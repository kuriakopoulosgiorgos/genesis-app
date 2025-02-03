package gr.uth.repositories;

import gr.uth.models.Attachment;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository {

    Optional<Attachment> findByReference(String reference);

    List<Attachment> findByReferences(List<String> references);

    Attachment persist(Attachment attachment);
}
