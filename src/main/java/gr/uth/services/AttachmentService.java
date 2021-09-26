package gr.uth.services;

import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface AttachmentService {

    Uni<List<Attachment>> uploadAttachments(AttachmentFormData attachmentFormData);
    Uni<Attachment> retrieveAttachmentByReference(String reference);
}
