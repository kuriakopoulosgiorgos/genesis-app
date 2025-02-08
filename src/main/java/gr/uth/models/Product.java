package gr.uth.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Table(name = "Products", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_description", columnList = "description")
})
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Product.deleteById",
                query = """
                        DELETE
                        FROM Product p
                        WHERE p.id = :id
                        """
        ),
        @NamedQuery(
                name = "Product.findByProductIds",
                query = """
                        SELECT p
                        FROM Product p
                        WHERE p.id IN :ids
                        """
        )
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private double price;
    @NotEmpty
    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> photos = new ArrayList<>();
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    private Model model;
    @Column(nullable = false)
    private String uploadedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Attachment> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Attachment> photos) {
        this.photos = photos;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
