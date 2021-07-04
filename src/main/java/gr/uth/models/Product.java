package gr.uth.models;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MongoEntity(database = "products")
public class Product extends ReactivePanacheMongoEntity {

    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotNull
    public double price;

    @Valid
    public Model model;
}
