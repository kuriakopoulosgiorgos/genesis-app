package gr.uth.resource;

import gr.uth.models.Product;
import gr.uth.repositories.ProductRepositoryImpl;
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
import test.TestUtil;


@ExtendWith(MockitoExtension.class)
public class ProductResourceTest extends BaseTest {

    private static final String BASE_PATH = "ProductResource/";
    private static final String REQUEST_PATH = BASE_PATH + "request/";
    private static final String RESPONSE_PATH = BASE_PATH + "response/";

    @Mock
    private ProductRepositoryImpl productRepository;

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
        // AND the product is valid
        // AND the product has no Model
        final var testId = 1L;
        var product = fromJson(REQUEST_PATH + "create_noModel.json", Product.class);

        Mockito.when(productRepository.persist(product))
                .thenReturn(Uni.createFrom().item(TestUtil.entityWithId(testId, product)));

        var actual = productResource.create(product)
                .subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertCompleted().getItem();

        // THEN the response should contain the provided product along with an id
        assertEquals(RESPONSE_PATH + "create_noModel.json", actual);
    }
}