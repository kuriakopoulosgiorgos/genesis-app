package gr.uth.services;

import gr.uth.dto.AttachmentFormData;
import gr.uth.dto.AttachmentMetaData;
import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Attachment;
import gr.uth.models.BinaryFile;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.BinaryFileRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;
    private BinaryFileRepository binaryFileRepository;

    public AttachmentServiceImpl() {
    }

    @Inject
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, BinaryFileRepository binaryFileRepository) {
        this.attachmentRepository = attachmentRepository;
        this.binaryFileRepository = binaryFileRepository;
    }

    @Override
    public List<Attachment> uploadAttachments(AttachmentFormData attachmentFormData) {

        List<Attachment> attachments = new ArrayList<>();
        var attachmentsReference = UUID.randomUUID().toString();
        for(int i = 0; i < attachmentFormData.getFile().size(); i++) {
            AttachmentMetaData attachmentMetaData = Objects.nonNull(attachmentFormData.getAttachmentMetaData()) ?
                    attachmentFormData.getAttachmentMetaData().get("" + (i + 1)) : null;
            String description = null;
            if(Objects.nonNull(attachmentMetaData)) {
                description = attachmentMetaData.description;
            }
            var fileUpload = attachmentFormData.getFile().get(i);
            try {
                var attachment = new Attachment();
                var filename = fileUpload.getFileName().orElse(UUID.randomUUID().toString());
                byte[] data = fileUpload.getContent(byte[].class);
                attachment.setName(getFileName(filename));
                attachment.setReference(attachmentsReference + "/" + filename);
                attachment.setDescription(description);
                attachment.setSize((long) data.length);
                attachment.setContentType(fileUpload.getMediaType().toString());

                BinaryFile binaryFile = new BinaryFile();
                binaryFile.setAttachment(attachment);

                binaryFile.setData(data);
                attachmentRepository.persist(attachment);
                binaryFileRepository.persist(binaryFile);
                attachments.add(binaryFile.getAttachment());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return attachments;
    }


    @Override
    public boolean deleteByReference(String reference) throws ValidationException {
        Attachment attachment = attachmentRepository.findByReference(reference).orElseThrow(() -> ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND));
        return binaryFileRepository.deleteById(attachment.getId());
    }

    @Override
    public Attachment retrieveAttachmentByReference(String reference) {
        return attachmentRepository.findByReference(reference).orElseThrow(() -> ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND));
    }

    private String getFileName(String filePath) {
        if(filePath.contains("/")) {
            var split = filePath.split("/");
            return split[split.length - 1];
        }
        return filePath;
    }
}
