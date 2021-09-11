package gr.uth.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.*;

@Table(name = "BinaryFiles")
@Entity
public class BinaryFile extends PanacheEntity {

    @Lob
    public byte[] data;

    @OneToOne
    @MapsId
    public Attachment attachment;
}
