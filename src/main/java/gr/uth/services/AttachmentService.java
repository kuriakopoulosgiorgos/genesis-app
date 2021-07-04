package gr.uth.services;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import io.smallrye.mutiny.Uni;

import java.util.Optional;

public interface AttachmentService {

    Attachment uploadAttachment(AttachmentFormData attachmentFormData);
    Uni<Optional<Attachment>> retrieveAttachmentByReference(String reference);
    GridFSDownloadStream retrieveFile(String fileReference);
}
