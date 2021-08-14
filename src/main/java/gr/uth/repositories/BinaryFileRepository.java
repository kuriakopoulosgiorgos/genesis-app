package gr.uth.repositories;

import gr.uth.models.BinaryFile;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

public interface BinaryFileRepository extends PanacheRepository<BinaryFile> {
}
