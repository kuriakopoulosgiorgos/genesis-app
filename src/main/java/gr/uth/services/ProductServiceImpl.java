package gr.uth.services;

import gr.uth.models.Product;
import gr.uth.repositories.ProductRepository;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Uni<List<Product>> findAll() {
        return productRepository.listAll();
    }

    @Override
    public Uni<Product> create(Product product) {
        if(Objects.nonNull(product.model)) {
            product.model.uploadDate = LocalDateTime.now();
        }
        return productRepository.persist(product);
    }

    @Override
    public Uni<Product> findById(String id) {
        return productRepository.findById(new ObjectId(id));
    }

    @Override
    public Uni<Boolean> deleteById(String id) {
        return productRepository.deleteById(new ObjectId(id));
    }
}
