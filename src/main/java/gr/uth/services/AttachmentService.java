package gr.uth.services;

import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;

import java.util.List;

public interface AttachmentService {

    List<Attachment> uploadAttachments(AttachmentFormData attachmentFormData);
    boolean deleteByReference(String reference);
    Attachment retrieveAttachmentByReference(String reference);
}
