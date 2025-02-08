package gr.uth.services;

import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Product;

import java.util.List;

public interface ProductService {

    Pageable<Product> findAll(
            ProductSortByField sortByField,
            SortByDirection sortByDirection,
            int page,
            int pageSize,
            List<Long> productCodes,
            String searchTerm
    );
    Product create(Product product) throws ValidationException;
    Product findById(Long id);
    boolean deleteById(Long id);
}
