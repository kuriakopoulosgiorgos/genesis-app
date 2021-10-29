package gr.uth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "Models")
@Entity
public class Model extends PanacheEntity {

    public LocalDateTime uploadDate;
    @JsonIgnore
    @OneToOne(mappedBy = "model")
    public Product product;
    @NotNull
    @OneToOne
    public Attachment attachment;
}
