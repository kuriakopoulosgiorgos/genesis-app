package gr.uth.models;

import jakarta.persistence.*;

@Table(name = "BinaryFiles")
@Entity
@NamedQueries({
        @NamedQuery(
                name = "BinaryFile.deleteById",
                query = """
                        DELETE
                        FROM BinaryFile b
                        WHERE b.id = :id
                        """
        )
})
public class BinaryFile {

    @Id
    @Column(name = "attachment_id", nullable = false)
    private Long id;

    @Lob
    private byte[] data;

    @OneToOne
    @MapsId
    private Attachment attachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}
