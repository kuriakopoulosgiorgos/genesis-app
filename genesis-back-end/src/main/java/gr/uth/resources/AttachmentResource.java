package gr.uth.resources;

import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import gr.uth.models.BinaryFile;
import gr.uth.services.AttachmentService;
import gr.uth.services.BinaryFileService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;

@Path("/attachments")
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
    public Uni<Attachment> uploadAttachment(@MultipartForm AttachmentFormData attachmentFormData) {
        return attachmentService.uploadAttachment(attachmentFormData);
    }

    @Blocking
    @Path("/{fileReference}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
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
