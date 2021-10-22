package gr.uth.resource;

import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Attachment;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;
import gr.uth.resources.ProductResource;
import gr.uth.services.ProductServiceImpl;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import test.BaseTest;
import test.utils.AttachmentTestUtil;
import test.utils.TestUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductResourceTest extends BaseTest {

    private static final String BASE_PATH = "ProductResource/";
    private static final String REQUEST_PATH = BASE_PATH + "request/";
    private static final String RESPONSE_PATH = BASE_PATH + "response/";

    private static final List<String> photoReferences = Arrays.asList("photo reference 1", "photo reference 2");

    private static final List<Attachment> validPhotos = Arrays.asList(
            AttachmentTestUtil.createAttachment("photo reference 1", "image/png"),
            AttachmentTestUtil.createAttachment("photo reference 2", "image/png")
    );

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductResource productResource;

    @BeforeEach
    public void initialize() {
        this.productResource = new ProductResource(productService);
    }

    @Test
    public void create_noModel() {
        // GIVEN a user is creating a product
        // When a new product is to be created
        // AND the product is valid
        // AND the product has no Model
        final var testId = 1L;
        var product = fromJson(REQUEST_PATH + "create_noModel.json", Product.class);

        TestUtil.withListResultQuery(attachmentRepository.find("reference", photoReferences), validPhotos);

        Mockito.when(productRepository.persist(product))
                .thenReturn(Uni.createFrom().item(TestUtil.entityWithId(testId, product)));

        var actual = productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertCompleted().getItem();

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
        var product = fromJson(REQUEST_PATH + "create_withModel.json", Product.class);

        TestUtil.withListResultQuery(attachmentRepository.find("reference", photoReferences), validPhotos);
        TestUtil.withFirstResultNullQuery(attachmentRepository.find("reference", attachmentReference));

        // THEN a validation exception should be thrown
        productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())

                .assertFailedWith(ValidationException.class, I18NMessage.ATTACHMENT_NOT_FOUND.getMessage()).getItem();
    }

    @Test
    public void create_withPhotos_attachmentNotFound() {
        // GIVEN a user is creating a product
        // WHEN a new Product is to be created
        // AND the product has 2 photos
        // AND one of the two photos is not found

        var product = fromJson(REQUEST_PATH + "create_withPhotos_attachmentNotFound.json", Product.class);

        TestUtil.withListResultQuery(attachmentRepository.find("reference", photoReferences),
                Collections.singletonList(AttachmentTestUtil.createAttachment("photo reference 1", "image/png")));

        // THEN a validation exception should be thrown
        productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())

                .assertFailedWith(ValidationException.class, I18NMessage.ATTACHMENT_NOT_FOUND.getMessage()).getItem();
    }

    @Test
    public void create_withPhotos_invalidImageType() {
        // GIVEN a user is creating a product
        // WHEN a new Product is to be created
        // AND the product has an invalid photo type
        final String photoReference = "photo reference 1";
        var product = fromJson(REQUEST_PATH + "create_withPhotos_invalidImageType.json", Product.class);

        TestUtil.withListResultQuery(attachmentRepository.find("reference", List.of(photoReference)),
                Collections.singletonList(AttachmentTestUtil.createAttachment(photoReference, "Text/plain")));

        // THEN a validation exception should be thrown
        productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())

                .assertFailedWith(ValidationException.class, I18NMessage.INVALID_PHOTO_TYPE.getMessage()).getItem();
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
        var product = fromJson(REQUEST_PATH + "create_withModel.json", Product.class);

        TestUtil.withListResultQuery(attachmentRepository.find("reference", photoReferences), validPhotos);

        TestUtil.withFirstResultQuery(attachmentRepository.find("reference", attachmentReference),
                AttachmentTestUtil.createAttachment(attachmentReference));

        Mockito.when(productRepository.persist(product))
                .thenReturn(Uni.createFrom().item(TestUtil.entityWithId(testId, product)));

        var actual = productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertCompleted().getItem();

        // THEN the response should contain the provided product along with an id and the attachment data
        assertEquals(RESPONSE_PATH + "create_withModel.json", actual, List.of("model.uploadDate"));
    }
}