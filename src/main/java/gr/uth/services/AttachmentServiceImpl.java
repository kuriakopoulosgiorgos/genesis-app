package gr.uth.services;

import gr.uth.dto.AttachmentFormData;
import gr.uth.interceptors.Transactional;
import gr.uth.models.Attachment;
import gr.uth.models.BinaryFile;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.BinaryFileRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@ApplicationScoped
public class AttachmentServiceImpl implements AttachmentService {

    final AttachmentRepository attachmentRepository;
    final BinaryFileRepository binaryFileRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, BinaryFileRepository binaryFileRepository) {
        this.attachmentRepository = attachmentRepository;
        this.binaryFileRepository = binaryFileRepository;
    }

    @Transactional
    @Override
    public Uni<Attachment> uploadAttachment(AttachmentFormData attachmentFormData) {
        var attachment = new Attachment();
        attachment.reference = UUID.randomUUID().toString();
        attachment.name = attachmentFormData.file.fileName();
        attachment.description = attachmentFormData.description;
        attachment.size = attachmentFormData.file.size();
        attachment.contentType  = attachmentFormData.file.contentType();

        BinaryFile binaryFile = new BinaryFile();
        binaryFile.attachment = attachment;

        try {
            binaryFile.data = Files.readAllBytes(attachmentFormData.file.uploadedFile());
        } catch (IOException e) {
            return Uni.createFrom().failure(e);
        }
        return attachmentRepository.persist(attachment).onItem()
                .transformToUni(savedAttachment -> binaryFileRepository.persist(binaryFile)
                        .map((_binaryFile -> savedAttachment)));
    }

    @Transactional
    @Override
    public Uni<Attachment> retrieveAttachmentByReference(String reference) {
        return attachmentRepository.find("reference", reference).firstResult();
    }
}
