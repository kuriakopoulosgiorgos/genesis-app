package gr.uth.repositories;

import gr.uth.models.BinaryFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
public class BinaryFileRepositoryImpl implements BinaryFileRepository {

    @PersistenceContext(name = "genesis-unit")
    private EntityManager em;

    @Override
    public BinaryFile persist(BinaryFile binaryFile) {
        em.persist(binaryFile);
        return binaryFile;
    }

    @Override
    public boolean deleteById(Long id) {
        return em.createNamedQuery("BinaryFile.deleteById", Integer.class)
                .setParameter("id", id).executeUpdate() > 0;
    }

    @Override
    public Optional<BinaryFile> findById(Long id) {
        return Optional.ofNullable(em.find(BinaryFile.class, id));
    }
}
