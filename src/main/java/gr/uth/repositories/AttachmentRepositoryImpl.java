package gr.uth.repositories;

import gr.uth.models.Attachment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AttachmentRepositoryImpl implements AttachmentRepository {

    @PersistenceContext(name = "genesis-unit")
    private EntityManager em;

    @Override
    public Optional<Attachment> findByReference(String reference) {
        return em.createNamedQuery("Attachment.findByReference", Attachment.class)
                .setParameter("reference", reference)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Attachment> findByReferences(List<String> references) {
        return em.createNamedQuery("Attachment.findByReferences", Attachment.class)
                .setParameter("references", references)
                .getResultList();
    }

    @Override
    public Attachment persist(Attachment attachment) {
        em.persist(attachment);
        return attachment;
    }
}
