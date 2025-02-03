package gr.uth.services;

import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.models.BinaryFile;
import gr.uth.repositories.BinaryFileRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BinaryFileServiceImpl implements BinaryFileService {

    @Inject
    private BinaryFileRepository binaryFileRepository;

    public BinaryFileServiceImpl() {
    }

    public BinaryFileServiceImpl(BinaryFileRepository binaryFileRepository) {
        this.binaryFileRepository = binaryFileRepository;
    }

    @Override
    public BinaryFile findById(Long id) {
        return binaryFileRepository.findById(id).orElseThrow(() -> ExceptionBuilder.fromMessage(I18NMessage.BINARY_FILE_NOT_FOUND));
    }
}
