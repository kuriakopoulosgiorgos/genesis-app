package gr.uth.resources;

import gr.uth.dto.AttachmentFormData;
import gr.uth.dto.AttachmentMetaData;
import gr.uth.dto.ValidationError;
import gr.uth.models.Attachment;
import gr.uth.models.BinaryFile;
import gr.uth.services.AttachmentService;
import gr.uth.services.BinaryFileService;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/attachments")
@Tag(name = "Attachment")
public class AttachmentResource {

    @Inject
    private AttachmentService attachmentService;

    @Inject
    private BinaryFileService binaryFileService;

    @Inject
    private Jsonb jsonb;

    public AttachmentResource() {
    }

    public AttachmentResource(AttachmentService attachmentService, BinaryFileService binaryFileService, Jsonb jsonb) {
        this.attachmentService = attachmentService;
        this.binaryFileService = binaryFileService;
        this.jsonb = jsonb;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public List<Attachment> uploadAttachments(
            @RequestBody(
                    description = "The attachment binary data",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA,
                            schema = @Schema(type = SchemaType.OBJECT, implementation = AttachmentFormData.class, format = "binary")
                    ),
                    required = true
            )
            List<EntityPart> entityParts
    ) {
        Map<String, AttachmentMetaData> attachmentMetaDataMap = entityParts.stream()
                .filter(entityPart -> "attachmentMetaData".equals(entityPart.getName()))
                .map(entityPart -> (Map<String, AttachmentMetaData>) jsonb.fromJson(entityPart.getContent(), new GenericType<Map<String, AttachmentMetaData>>() {}.getType()))
                .findFirst()
                .orElse(null);
        List<EntityPart> files = entityParts.stream()
                .filter(entityPart -> "file".equals(entityPart.getName()))
                .toList();
        return attachmentService.uploadAttachments(new AttachmentFormData(attachmentMetaDataMap, files));
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
    public Response deleteByReference(String fileReference) {
        return attachmentService.deleteByReference(fileReference)
                ? Response.status(Response.Status.NO_CONTENT).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

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
    public Response retrieveFile(@PathParam("fileReference") String fileReference) {
        Attachment attachment = attachmentService.retrieveAttachmentByReference(fileReference);
        BinaryFile binaryFile = binaryFileService.findById(attachment.getId());
        return fileResponse(attachment, binaryFile);
    }

    private Response fileResponse(Attachment attachment, BinaryFile binaryFile) {
        return Response.ok(binaryFile.getData())
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + attachment.getName())
                .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
                .build();
    }
}
