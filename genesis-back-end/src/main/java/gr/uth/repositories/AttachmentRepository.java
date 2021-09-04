package gr.uth.repositories;

import gr.uth.models.Attachment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

public interface AttachmentRepository extends PanacheRepository<Attachment> {}
