package gr.uth.services;

import gr.uth.models.BinaryFile;
import gr.uth.repositories.BinaryFileRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BinaryFileServiceImpl implements BinaryFileService {

    @Inject
    BinaryFileRepository binaryFileRepository;

    public BinaryFileServiceImpl(BinaryFileRepository binaryFileRepository) {
        this.binaryFileRepository = binaryFileRepository;
    }

    @Override
    public Uni<BinaryFile> findById(Long id) {
        return binaryFileRepository.findById(id);
    }
}
