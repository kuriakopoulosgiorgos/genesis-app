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
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class AttachmentServiceImpl implements AttachmentService {

    final AttachmentRepository attachmentRepository;
    final BinaryFileRepository binaryFileRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, BinaryFileRepository binaryFileRepository) {
        this.attachmentRepository = attachmentRepository;
        this.binaryFileRepository = binaryFileRepository;
    }

    @ReactiveTransactional
    @Override
    public Uni<List<Attachment>> uploadAttachments(AttachmentFormData attachmentFormData) {

        List<Attachment> attachments = new ArrayList<>();
        List<BinaryFile> binaryFiles = new ArrayList<>();
        var attachmentsReference = UUID.randomUUID().toString();
        for(int i = 0; i < attachmentFormData.file.size(); i++) {
            AttachmentMetaData attachmentMetaData = Objects.nonNull(attachmentFormData.attachmentMetaData) ?
                    attachmentFormData.attachmentMetaData.get("" + (i + 1)) : null;
            String description = null;
            if(Objects.nonNull(attachmentMetaData)) {
                description = attachmentMetaData.description;
            }
            var fileUpload = attachmentFormData.file.get(i);
            var attachment = new Attachment();
            attachment.name = getFileName(fileUpload.fileName());
            attachment.reference = attachmentsReference + "/" + fileUpload.fileName();
            attachment.description = description;
            attachment.size = fileUpload.size();
            attachment.contentType  = fileUpload.contentType();

            BinaryFile binaryFile = new BinaryFile();
            binaryFile.attachment = attachment;

            try {
                binaryFile.data = Files.readAllBytes(fileUpload.uploadedFile());
                attachments.add(attachment);
                binaryFiles.add(binaryFile);
            } catch (IOException e) {
                return (Uni.createFrom().failure(e));
            }
        }
        List<Uni<Attachment>> attachmentsUnis = IntStream.range(0, attachments.size())
                .mapToObj(i -> attachmentRepository.persist(attachments.get(i)).onItem()
                        .transformToUni(savedAttachment -> binaryFileRepository.persist(binaryFiles.get(i))
                                .map(_binaryFile -> savedAttachment))).collect(Collectors.toList());

        return Uni.combine().all()
                .unis(attachmentsUnis)
                .combinedWith(Attachment.class, ArrayList::new);
    }


    @ReactiveTransactional
    @Override
    public Uni<Boolean> deleteByReference(String reference) throws ValidationException {
        return attachmentRepository.find("reference", reference).firstResult()
                .onItem()
                .ifNull().failWith(ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND))
                .onItem()
                .transformToUni(attachment -> binaryFileRepository.deleteById(attachment.id));
    }

    @ReactiveTransactional
    @Override
    public Uni<Attachment> retrieveAttachmentByReference(String reference) {
        return attachmentRepository.find("reference", reference).firstResult();
    }

    private String getFileName(String filePath) {
        if(filePath.contains("/")) {
            var split = filePath.split("/");
            return split[split.length - 1];
        }
        return filePath;
    }
}
