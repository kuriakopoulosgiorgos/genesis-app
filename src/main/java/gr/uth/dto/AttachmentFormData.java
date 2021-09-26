package gr.uth.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

public class AttachmentFormData {

    @RestForm
    @PartType(MediaType.APPLICATION_JSON)
    public Map<String, AttachmentMetaData> attachmentMetaData;

    @Schema(not = FileUpload.class)
    @RestForm()
    public List<FileUpload> file;
}
