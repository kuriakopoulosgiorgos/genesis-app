package gr.uth.services;

import gr.uth.models.BinaryFile;
import io.smallrye.mutiny.Uni;

public interface BinaryFileService {

    Uni<BinaryFile> findById(Long id);
}
