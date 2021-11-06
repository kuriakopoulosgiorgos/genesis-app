package gr.uth.resource;

import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.resources.AttachmentResource;
import gr.uth.services.AttachmentServiceImpl;
import gr.uth.services.BinaryFileServiceImpl;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.utils.TestUtil;

@ExtendWith(MockitoExtension.class)
public class AttachmentResourceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @InjectMocks
    private BinaryFileServiceImpl binaryFileService;

    private AttachmentResource attachmentResource;

    @BeforeEach
    public void initialize() {
        this.attachmentResource = new AttachmentResource(attachmentService, binaryFileService);
    }

    @Test
    public void deleteByReference_attachmentNotFound() {
        // GIVEN the attachment delete
        // When a user is deleting an attachment by reference
        // AND the attachment is not found

        final var attachmentReference = "not existing attachment reference";
        TestUtil.withFirstResultNullQuery(attachmentRepository.find("reference", attachmentReference));

        // THEN a validation exception should be thrown
        attachmentResource.deleteByReference(attachmentReference)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertFailedWith(ValidationException.class, I18NMessage.ATTACHMENT_NOT_FOUND.getMessage());
    }
}