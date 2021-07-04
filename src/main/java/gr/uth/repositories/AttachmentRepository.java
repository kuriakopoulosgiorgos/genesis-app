package gr.uth.repositories;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

public interface AttachmentRepository extends ReactivePanacheMongoRepository<Attachment> {

    Attachment uploadAttachment(AttachmentFormData attachmentFormData);
    GridFSDownloadStream retrieveFile(String fileReference);
}
