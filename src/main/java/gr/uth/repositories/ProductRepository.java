package gr.uth.repositories;

import gr.uth.models.Product;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements ReactivePanacheMongoRepository<Product> {
}
