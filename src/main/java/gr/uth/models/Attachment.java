package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;

@Table(name = "Attachments")
@Entity
public class Attachment extends PanacheEntity {

    @Column(unique = true)
    public String reference;
    public String name;
    public String description;
    public Long size;
    public String contentType;
}
