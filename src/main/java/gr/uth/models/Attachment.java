package gr.uth.models;

import jakarta.persistence.*;

@Table(name = "Attachments")
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Attachment.findByReference",
                query = """
                        SELECT a
                        FROM Attachment a
                        WHERE a.reference = :reference
                        """
        ),
        @NamedQuery(
                name = "Attachment.findByReferences",
                query = """
                        SELECT a
                        FROM Attachment a
                        WHERE a.reference IN :references
                        """
        ),
})
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String reference;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
