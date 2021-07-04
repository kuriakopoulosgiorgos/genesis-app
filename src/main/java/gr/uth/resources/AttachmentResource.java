package gr.uth.resources;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import gr.uth.services.AttachmentService;
import io.smallrye.common.annotation.Blocking;
import org.jboss.resteasy.reactive.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Blocking
@Path("/attachments")
public class AttachmentResource {

    @Inject
    AttachmentService attachmentService;

    public AttachmentResource(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @Blocking
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Attachment uploadAttachment(@MultipartForm AttachmentFormData attachmentFormData) {
        return attachmentService.uploadAttachment(attachmentFormData);
    }

    @Blocking
    @Path("/{fileReference}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response retrieveFile(String fileReference) throws IOException {
        var oAttachment = attachmentService
                .retrieveAttachmentByReference(fileReference).await().indefinitely();
        if(oAttachment.isPresent()) {
            var attachment = oAttachment.get();
            GridFSDownloadStream gridFSDownloadStream = attachmentService.retrieveFile(fileReference);

            return Response.ok(new ByteArrayInputStream(gridFSDownloadStream.readAllBytes()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + attachment.name)
                    .header(HttpHeaders.CONTENT_TYPE, attachment.contentType)
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
