package gr.uth.services;

import gr.uth.GenesisUser;
import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
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
    final GenesisUser user;

    public ProductServiceImpl(ProductRepository productRepository, AttachmentRepository attachmentRepository, GenesisUser user) {
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
        this.user = user;
    }

    @Override
    @ReactiveTransactional
    public Uni<Pageable<Product>> findAll(ProductSortByField sortByField, SortByDirection sortByDirection,
                                          int page, int pageSize,
                                          List<Long> productCodes, String searchTerm) {
        var sortByColumn = Objects.nonNull(sortByField) ? sortByField.name() : "id";
        var sort = Sort.by(sortByColumn);
        sort.direction(sortByDirection == SortByDirection.asc ? Sort.Direction.Ascending : Sort.Direction.Descending);
        var jpqlBuilder = new StringBuilder("1 = 1 ");
        var parameters = new Parameters();
        if(Objects.nonNull(productCodes) && !productCodes.isEmpty()) {
            jpqlBuilder.append("AND id IN :productsCodes ");
            parameters.and("productsCodes", productCodes);
        }
        if(Objects.nonNull(searchTerm)) {
            jpqlBuilder.append("AND (LOWER(name) LIKE CONCAT('%',LOWER(:name),'%') OR ");
            jpqlBuilder.append("LOWER(description) LIKE CONCAT('%',LOWER(:description),'%')) ");
            parameters.and("name", searchTerm).and("description", searchTerm);
        }

        var query = productRepository.find(jpqlBuilder.toString(), sort, parameters);
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

    @ReactiveTransactional
    @Override
    public Uni<Product> create(Product product) throws ValidationException {
        product.uploadedBy = user.getUsername();
        var model = product.model;

        var photoReferences = product.photos.stream()
                .map((attachment -> attachment.reference))
                .collect(Collectors.toList());

       return attachmentRepository.find(
               "reference IN :references", Parameters.with("references", photoReferences))
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
                       return attachmentRepository.find("reference", attachmentReference).firstResult()
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

    @ReactiveTransactional
    @Override
    public Uni<Boolean> deleteById(Long id) {
        return productRepository.deleteById(id);
    }
}
