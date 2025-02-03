package gr.uth.repositories;

import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.models.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext(name = "genesis-unit")
    private EntityManager em;

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    @Override
    public boolean deleteById(Long id) {
        return em.createQuery("Product.deleteById", Integer.class).executeUpdate() > 0;
    }

    @Override
    public long findAllCount(List<Long> productCodes, String searchTerm) {
        return buildAndBindFindAllQuery(true, null, null, productCodes, searchTerm, Long.class)
                .getSingleResult();
    }

    @Override
    public List<Product> findAll(
            ProductSortByField sortByField,
            SortByDirection sortByDirection,
            int page,
            int pageSize,
            List<Long> productCodes,
            String searchTerm
    ) {
        return buildAndBindFindAllQuery(false, sortByField, sortByDirection, productCodes, searchTerm, Product.class)
                .setFirstResult((page -1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Product> findByIds(List<Long> ids) {
        return em.createNamedQuery("Product.findByProductIds", Product.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public Product persist(Product product) {
        em.persist(product);
        return product;
    }

    private <X> TypedQuery<X> buildAndBindFindAllQuery(
            boolean isCountQuery,
            ProductSortByField sortByField,
            SortByDirection sortByDirection,
            List<Long> productCodes,
            String searchTerm,
            Class<X> clazz
    ) {
        var jpqlBuilder = new StringBuilder("SELECT ");
        if (isCountQuery) {
            jpqlBuilder.append("COUNT(product) ");
        } else {
            jpqlBuilder.append("product ");
        }
        jpqlBuilder.append("FROM Product product WHERE 1 = 1 ");
        List<Consumer<TypedQuery<X>>> paramBinders = new ArrayList<>();
        if(Objects.nonNull(productCodes) && !productCodes.isEmpty()) {
            jpqlBuilder.append("AND product.id IN :productsCodes ");
            paramBinders.add(typedQuery -> typedQuery.setParameter("productsCodes", productCodes));
        }
        if (Objects.nonNull(searchTerm)) {
            jpqlBuilder.append("AND (LOWER(product.name) LIKE CONCAT('%',LOWER(:name),'%') OR ");
            jpqlBuilder.append("LOWER(product.description) LIKE CONCAT('%',LOWER(:description),'%')) ");
            paramBinders.add(typedQuery -> typedQuery.setParameter("name", searchTerm));
            paramBinders.add(typedQuery -> typedQuery.setParameter("description", searchTerm));
        }
        if (!isCountQuery) {
            String sortByColumn = Objects.nonNull(sortByField) ? sortByField.name() : "id";
            jpqlBuilder.append("ORDER BY product.").append(sortByColumn).append(" ").append(sortByDirection.name());
        }
        TypedQuery<X> typedQuery = em.createQuery(jpqlBuilder.toString(), clazz);
        paramBinders.forEach(e -> e.accept(typedQuery));
        return typedQuery;
    }
}
