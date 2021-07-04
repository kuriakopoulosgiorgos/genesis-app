package gr.uth.services;

import com.mongodb.client.gridfs.GridFSDownloadStream;
import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import gr.uth.repositories.AttachmentRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class AttachmentServiceImpl implements AttachmentService {

    final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    @Transactional
    public Attachment uploadAttachment(AttachmentFormData attachmentFormData) {
        return attachmentRepository.uploadAttachment(attachmentFormData);
    }

    @Override
    public Uni<Optional<Attachment>> retrieveAttachmentByReference(String reference) {
        return attachmentRepository.find("reference", reference).singleResultOptional();
    }

    @Override
    public GridFSDownloadStream retrieveFile(String fileReference) {
        return attachmentRepository.retrieveFile(fileReference);
    }
}
