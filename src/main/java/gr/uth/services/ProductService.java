package gr.uth.services;

import gr.uth.exceptions.ValidationException;
import gr.uth.models.Product;
import io.smallrye.mutiny.Uni;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {

    Uni<List<Product>> findAll();
    Uni<Product> create(@Valid Product product) throws ValidationException;
    Uni<Product> findById(Long id);
    Uni<Boolean> deleteById(Long id);
}
