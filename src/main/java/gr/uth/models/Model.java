package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Table(name = "Entities")
@Entity
public class Model extends PanacheEntity {

    @NotBlank
    public String fileReference;
    @NotBlank
    public String name;
    public String description;
    public LocalDateTime uploadDate;
    @OneToOne(mappedBy = "model")
    public Product product;
    @OneToOne
    public Attachment attachment;
}
