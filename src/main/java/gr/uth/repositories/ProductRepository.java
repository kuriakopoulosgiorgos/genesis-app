package gr.uth.repositories;

import gr.uth.models.Product;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

public interface ProductRepository extends ReactivePanacheMongoRepository<Product> {
}
