package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "Products", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_description", columnList = "description")
})
@Entity
public class Product extends PanacheEntity {

    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotNull
    public double price;

    @NotEmpty
    @OneToMany(fetch = FetchType.EAGER)
    public List<Attachment> photos;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    public Model model;

    @Column(nullable = false)
    public String uploadedBy;
}
