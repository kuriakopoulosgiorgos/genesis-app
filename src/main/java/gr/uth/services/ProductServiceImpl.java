package gr.uth.services;

import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.interceptors.Transactional;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final AttachmentRepository attachmentRepository;

    public ProductServiceImpl(ProductRepository productRepository, AttachmentRepository attachmentRepository) {
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Uni<List<Product>> findAll() {
        return productRepository.listAll();
    }

    @Transactional
    @Override
    public Uni<Product> create(Product product) throws ValidationException {
        var model = product.model;
        if(Objects.nonNull(model)) {
            var attachmentReference = model.attachment.reference;
            return  attachmentRepository.find("reference", attachmentReference).firstResult()
                    .onItem()
                    .ifNull().failWith(ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND))
                    .onItem()
                    .transformToUni(attachment -> {
                        model.uploadDate = LocalDateTime.now();
                        model.attachment = attachment;
                        return productRepository.persist(product);
                    });
        }
        return productRepository.persist(product);
    }

    @Override
    public Uni<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Uni<Boolean> deleteById(Long id) {
        return productRepository.deleteById(id);
    }
}
