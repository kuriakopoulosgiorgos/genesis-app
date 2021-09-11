package gr.uth.repositories;

import gr.uth.models.Product;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

public interface ProductRepository extends PanacheRepository<Product> {}
