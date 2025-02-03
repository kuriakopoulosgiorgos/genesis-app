package gr.uth.services;

import gr.uth.GenesisUser;
import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.exceptions.ExceptionBuilder;
import gr.uth.exceptions.I18NMessage;
import gr.uth.exceptions.ValidationException;
import gr.uth.models.Attachment;
import gr.uth.models.Model;
import gr.uth.models.Product;
import gr.uth.repositories.AttachmentRepository;
import gr.uth.repositories.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final List<String> VALID_IMAGE_TYPES = List.of(
            "image/png", "image/jpeg"
    );

    private ProductRepository productRepository;
    private AttachmentRepository attachmentRepository;
    private GenesisUser user;

    public ProductServiceImpl() {
    }

    @Inject
    public ProductServiceImpl(ProductRepository productRepository, AttachmentRepository attachmentRepository, GenesisUser user) {
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
        this.user = user;
    }

    @Override
    public Pageable<Product> findAll(
            ProductSortByField sortByField,
            SortByDirection sortByDirection,
            int page,
            int pageSize,
            List<Long> productCodes,
            String searchTerm
    ) {
        long count = productRepository.findAllCount(productCodes, searchTerm);
        List<Product> products = productRepository.findAll(sortByField, sortByDirection, page, pageSize, productCodes, searchTerm);
        return new Pageable<>(products, count);
    }

    @Override
    public Product create(Product product) throws ValidationException {
        product.setUploadedBy(user.getUsername());
        var photoReferences = product.getPhotos().stream()
                .map((Attachment::getReference))
                .collect(Collectors.toList());
       List<Attachment> attachments = attachmentRepository.findByReferences(photoReferences);
        if (attachments.size() != photoReferences.size()) {
            throw ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND);
        }
        if (attachments.stream().anyMatch(attachment -> !VALID_IMAGE_TYPES.contains(attachment.getContentType()))) {
            throw ExceptionBuilder.fromMessage(I18NMessage.INVALID_PHOTO_TYPE);
        }
        product.setPhotos(attachments);
        Model model = product.getModel();
        if (Objects.nonNull(model)) {
            String attachmentReference = model.getAttachment().getReference();
            Attachment attachment = attachmentRepository.findByReference(attachmentReference)
                    .orElseThrow(() -> ExceptionBuilder.fromMessage(I18NMessage.ATTACHMENT_NOT_FOUND));
            model.setUploadDate(LocalDateTime.now());
            model.setAttachment(attachment);
        }
        return productRepository.persist(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> ExceptionBuilder.fromMessage(I18NMessage.PRODUCT_NOT_FOUND));
    }

    @Override
    public boolean deleteById(Long id) {
        return productRepository.deleteById(id);
    }
}
