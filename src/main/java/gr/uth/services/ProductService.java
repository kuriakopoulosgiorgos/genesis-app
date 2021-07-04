package gr.uth.services;

import gr.uth.models.Product;
import io.smallrye.mutiny.Uni;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {

    Uni<List<Product>> findAll();
    Uni<Product> create(@Valid Product product);
    Uni<Product> findById(String id);
    Uni<Boolean> deleteById(String id);
}
