package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;

@Table(name = "Attachments")
@Entity
public class Attachment extends PanacheEntity {

    @Column(unique = true)
    public String reference;
    @Column(nullable = false)
    public String name;
    public String description;
    @Column(nullable = false)
    public Long size;
    @Column(nullable = false)
    public String contentType;
}
