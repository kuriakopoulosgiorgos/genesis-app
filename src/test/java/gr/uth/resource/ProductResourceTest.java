package gr.uth.resource;

import gr.uth.GenesisUser;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Attachment;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;
import gr.uth.resources.ProductResource;
import gr.uth.services.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import test.BaseTest;
import test.utils.AttachmentTestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductResourceTest extends BaseTest {

    private static final String BASE_PATH = "ProductResource/";
    private static final String REQUEST_PATH = BASE_PATH + "request/";
    private static final String RESPONSE_PATH = BASE_PATH + "response/";

    private static final List<Attachment> validPhotos = Arrays.asList(
            AttachmentTestUtil.createAttachment("photo reference 1", "image/png"),
            AttachmentTestUtil.createAttachment("photo reference 2", "image/png")
    );

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private GenesisUser user;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductResource productResource;

    @BeforeEach
    public void initialize() {
        Mockito.when(user.getUsername()).thenReturn("user");
        this.productResource = new ProductResource(productService);
    }

    @Test
    public void create_noModel() {
        // GIVEN a user is creating a product
        // When a new product is to be created
        // AND the product is valid
        // AND the product has no Model
        final var testId = 1L;
        Product product = fromJson(REQUEST_PATH + "create_noModel.json", Product.class);
        Mockito.when(attachmentRepository.findByReferences(Mockito.anyList())).thenReturn(validPhotos);
        product.setId(testId);
        Mockito.when(productRepository.persist(product)).thenReturn(product);
        Product actual = productResource.create(product);
        // THEN the response should contain the provided product along with an id
        assertEquals(RESPONSE_PATH + "create_noModel.json", actual);
    }

    @Test
    public void create_withModel_attachmentNotFound() {
        // GIVEN a user is creating a product
        // WHEN a new Product is to be created
        // AND the product is valid
        // AND the product has a Model
        // AND the attachment is not found

        final var attachmentReference = "reference"; // representing a none existing reference
        Product product = fromJson(REQUEST_PATH + "create_withModel.json", Product.class);
        Mockito.when(attachmentRepository.findByReferences(Mockito.anyList())).thenReturn(validPhotos);
        Mockito.when(attachmentRepository.findByReference(attachmentReference)).thenReturn(Optional.empty());
        // THEN a validation exception should be thrown
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> productResource.create(product));
        Assertions.assertEquals(I18NMessage.ATTACHMENT_NOT_FOUND.getMessage(), validationException.getValidationError().getMessage());
    }

    @Test
    public void create_withPhotos_attachmentNotFound() {
        // GIVEN a user is creating a product
        // WHEN a new Product is to be created
        // AND the product has 2 photos
        // AND one of the two photos is not found

        Product product = fromJson(REQUEST_PATH + "create_withPhotos_attachmentNotFound.json", Product.class);
        Mockito.when(attachmentRepository.findByReferences(Mockito.anyList()))
                .thenReturn(List.of(AttachmentTestUtil.createAttachment("photo reference 1", "image/png")));
        // THEN a validation exception should be thrown
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> productResource.create(product));
        Assertions.assertEquals(I18NMessage.ATTACHMENT_NOT_FOUND.getMessage(), validationException.getValidationError().getMessage());
    }

    @Test
    public void create_withPhotos_invalidImageType() {
        // GIVEN a user is creating a product
        // WHEN a new Product is to be created
        // AND the product has an invalid photo type
        final String photoReference = "photo reference 1";
        Product product = fromJson(REQUEST_PATH + "create_withPhotos_invalidImageType.json", Product.class);
        Mockito.when(attachmentRepository.findByReferences(Mockito.anyList()))
                .thenReturn(List.of(AttachmentTestUtil.createAttachment(photoReference, "Text/plain")));
        // THEN a validation exception should be thrown
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> productResource.create(product));
        Assertions.assertEquals(I18NMessage.INVALID_PHOTO_TYPE.getMessage(), validationException.getValidationError().getMessage());
    }

    @Test
    public void create_withModel() {
        // GIVEN a user is creating a product
        // AND the product is valid
        // AND the product has a Model
        // AND the attachment is found
        // WHEN a new Product is to be created
        final var testId = 1L;
        final var attachmentReference = "reference"; // representing a none existing reference
        Product product = fromJson(REQUEST_PATH + "create_withModel.json", Product.class);
        Mockito.when(attachmentRepository.findByReferences(Mockito.anyList()))
                .thenReturn(validPhotos);
        Mockito.when(attachmentRepository.findByReference(attachmentReference)).thenReturn(Optional.of(AttachmentTestUtil.createAttachment(attachmentReference)));
        product.setId(testId);
        Mockito.when(productRepository.persist(product)).thenReturn(product);
        Product actual = productResource.create(product);
        // THEN the response should contain the provided product along with an id and the attachment data
        assertEquals(RESPONSE_PATH + "create_withModel.json", actual, List.of("model.uploadDate"));
    }
}
