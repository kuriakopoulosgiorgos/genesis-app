package gr.uth.services;

import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.interceptors.Transactional;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    private static final List<String> VALID_IMAGE_TYPES = List.of(
            "image/png", "image/jpeg"
    );

    final ProductRepository productRepository;
    final AttachmentRepository attachmentRepository;

    public ProductServiceImpl(ProductRepository productRepository, AttachmentRepository attachmentRepository) {
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    @Transactional
    public Uni<Pageable<Product>> findAll(ProductSortByField sortByField, SortByDirection sortByDirection,
                                          int page, int pageSize) {
        var sortByColumn = Objects.nonNull(sortByField) ? sortByField.name() : "id";
        var sort = Sort.by(sortByColumn);
        sort.direction(sortByDirection == SortByDirection.asc ? Sort.Direction.Ascending : Sort.Direction.Descending);
        var query = productRepository.findAll(sort);
        var count = query.count();
        var products = query.page(Page.of(page - 1, pageSize)).list();
        return Uni.combine().all().unis(count, products).asTuple()
                .onItem().transform(results -> {
                    var productPageable = new Pageable<Product>();
                    productPageable.data = results.getItem2();
                    productPageable.totalCount = results.getItem1();
                    return productPageable;
                });
    }

    @Transactional
    @Override
    public Uni<Product> create(Product product) throws ValidationException {
        var model = product.model;

        var photoReferences = product.photos.stream()
                .map((attachment -> attachment.reference))
                .collect(Collectors.toList());

       return attachmentRepository.find("reference", photoReferences)
                .list().onItem().invoke((attachments) -> {

                    if(attachments.size() != photoReferences.size()) {
                        throw ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND);
                    }

                    if(attachments.stream().anyMatch(attachment -> !VALID_IMAGE_TYPES.contains(attachment.contentType))) {
                        throw ExceptionBuilder.fromMessage(I18NMessage.INVALID_PHOTO_TYPE);
                    }

                    product.photos = attachments;
                })
               .onItem()
               .transformToUni((_v) -> {
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
               });
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
