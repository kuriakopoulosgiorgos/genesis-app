package gr.uth.repositories;

import gr.uth.models.BinaryFile;

import java.util.Optional;

public interface BinaryFileRepository {

    BinaryFile persist(BinaryFile binaryFile);

    boolean deleteById(Long id);

    Optional<BinaryFile> findById(Long id);
}
