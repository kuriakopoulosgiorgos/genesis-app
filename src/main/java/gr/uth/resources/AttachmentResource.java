package gr.uth.resources;

import gr.uth.dto.AttachmentFormData;
import gr.uth.dto.ValidationError;
import gr.uth.models.Attachment;
import gr.uth.models.BinaryFile;
import gr.uth.services.AttachmentService;
import gr.uth.services.BinaryFileService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.List;

@Path("/attachments")
@Tag(name = "Attachment")
public class AttachmentResource {

    @Inject
    AttachmentService attachmentService;

    @Inject
    BinaryFileService binaryFileService;

    public AttachmentResource(AttachmentService attachmentService, BinaryFileService binaryFileService) {
        this.attachmentService = attachmentService;
        this.binaryFileService = binaryFileService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<List<Attachment>> uploadAttachments(
            @RequestBody(
                    description = "The attachment binary data",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA,
                            schema = @Schema(type = SchemaType.OBJECT, implementation = AttachmentFormData.class, format = "binary")
                    ),
                    required = true
            )
            @MultipartForm AttachmentFormData attachmentFormData) {
        return attachmentService.uploadAttachments(attachmentFormData);
    }

    @DELETE
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.TEXT_PLAIN)
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Attachment deleted",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Attachment not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ValidationError.class))
            ),
    })
    public Uni<Response> deleteByReference(String fileReference) {

        return attachmentService.deleteByReference(fileReference).map(isDeleted ->
                isDeleted ? Response.status(Response.Status.NO_CONTENT).build()
                        : Response.status(Response.Status.NOT_FOUND).build());
    }

    @Blocking
    @Path("{fileReference:.+}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA, schema = @Schema(format = "binary"))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "No Attachment found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
    })
    public Uni<Response> retrieveFile(String fileReference) {

        return attachmentService
                .retrieveAttachmentByReference(fileReference)
                .onItem().ifNotNull()
                .transformToUni(attachment ->
                    binaryFileService.findById(attachment.id)
                            .flatMap((binaryFile) -> fileResponse(attachment, binaryFile))
                .onItem()
                .ifNull()
                .continueWith(Response.status(Response.Status.NOT_FOUND).build()));
    }

    private Uni<Response> fileResponse(Attachment attachment, BinaryFile binaryFile) {


        return Uni.createFrom().item(Response.ok(new ByteArrayInputStream(binaryFile.data))
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + attachment.name)
                .header(HttpHeaders.CONTENT_TYPE, attachment.contentType)
                .build());
    }
}
