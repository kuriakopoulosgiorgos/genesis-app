package gr.uth.dto;

import jakarta.ws.rs.core.EntityPart;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public class AttachmentFormData {

    private Map<String, AttachmentMetaData> attachmentMetaData;
    @Schema(not = EntityPart.class)
    private List<EntityPart> file;

    public AttachmentFormData(Map<String, AttachmentMetaData> attachmentMetaData, List<EntityPart> file) {
        this.attachmentMetaData = attachmentMetaData;
        this.file = file;
    }

    public Map<String, AttachmentMetaData> getAttachmentMetaData() {
        return attachmentMetaData;
    }

    public void setAttachmentMetaData(Map<String, AttachmentMetaData> attachmentMetaData) {
        this.attachmentMetaData = attachmentMetaData;
    }

    public List<EntityPart> getFile() {
        return file;
    }

    public void setFile(List<EntityPart> file) {
        this.file = file;
    }
}
