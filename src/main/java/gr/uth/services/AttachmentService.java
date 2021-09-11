package gr.uth.services;

import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import io.smallrye.mutiny.Uni;

public interface AttachmentService {

    Uni<Attachment> uploadAttachment(AttachmentFormData attachmentFormData);
    Uni<Attachment> retrieveAttachmentByReference(String reference);
}
