package gr.uth.dto;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import javax.ws.rs.core.MediaType;

public class AttachmentFormData {
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String description;

    @RestForm("file")
    public FileUpload file;
}
