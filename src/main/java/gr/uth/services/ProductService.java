package gr.uth.services;

import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Product;
import io.smallrye.mutiny.Uni;

import javax.validation.Valid;

public interface ProductService {

    Uni<Pageable<Product>> findAll(ProductSortByField sortByField, SortByDirection sortByDirection,
                                   int page, int pageSize,
                                   String searchTerm);
    Uni<Product> create(@Valid Product product) throws ValidationException;
    Uni<Product> findById(Long id);
    Uni<Boolean> deleteById(Long id);
}
