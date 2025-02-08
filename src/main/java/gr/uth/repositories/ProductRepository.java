package gr.uth.repositories;

import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    boolean deleteById(Long id);

    long findAllCount(List<Long> productCodes, String searchTerm);

    List<Product> findAll(
            ProductSortByField sortByField,
            SortByDirection sortByDirection,
            int page,
            int pageSize,
            List<Long> productCodes,
            String searchTerm
    );

    List<Product> findByIds(List<Long> ids);

    Product persist(Product product);
}
