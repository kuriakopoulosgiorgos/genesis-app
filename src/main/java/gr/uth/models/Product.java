package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "Products")
@Entity
public class Product extends PanacheEntity {

    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotNull
    public double price;

    @Valid
    @OneToOne
    public Model model;
}
