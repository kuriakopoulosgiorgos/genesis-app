package gr.uth.resource;

import gr.uth.exceptions.ValidationException;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.resources.AttachmentResource;
import gr.uth.services.AttachmentServiceImpl;
import gr.uth.services.BinaryFileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        this.attachmentResource = new AttachmentResource(attachmentService, binaryFileService, null);
    }

    @Test
    public void deleteByReference_attachmentNotFound() {
        // GIVEN the attachment delete
        // When a user is deleting an attachment by reference
        // AND the attachment is not found

        final var attachmentReference = "not existing attachment reference";
        Mockito.when(attachmentRepository.findByReference(attachmentReference)).thenReturn(Optional.empty());

        // THEN a validation exception should be thrown
        Assertions.assertThrows(ValidationException.class, () -> attachmentResource.deleteByReference(attachmentReference).close());
    }
}