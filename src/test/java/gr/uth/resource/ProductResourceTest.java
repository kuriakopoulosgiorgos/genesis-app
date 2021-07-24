package gr.uth.resource;

import gr.uth.models.Product;
import gr.uth.repositories.ProductRepositoryImpl;
import gr.uth.resources.ProductResource;
import gr.uth.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ProductResourceTest {

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
    public void create() {

        Product product = new Product();
        product.name = "name";
        product.description = "description";
        product.price = 1.0;

        productResource.create(product);

        Mockito.verify(productRepository).persist(product);
    }
}