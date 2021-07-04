package gr.uth.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import gr.uth.dto.AttachmentFormData;
import gr.uth.models.Attachment;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.UUID;

@ApplicationScoped
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private static final String DEFAULT_MONGO_CLIENT_NAME = "camelMongoClient";
    private static final String ENDPOINT_URL = "mongodb-gridfs:%s?database=%s";
    private static final String CREATE_OPERATION = "&operation=create";
    private static final String FIND_ONE_OPERATION = "&operation=findOne";

    @Inject
    MongoClient mongoClient;

    @Inject
    ProducerTemplate producerTemplate;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public AttachmentRepositoryImpl(MongoClient mongoClient, ProducerTemplate producerTemplate) {
        this.mongoClient = mongoClient;
        this.producerTemplate = producerTemplate;
    }

    @Override
    public Attachment uploadAttachment(AttachmentFormData attachmentFormData) {
        var headers = new HashMap<String, Object>();
        String fileReference = UUID.randomUUID().toString();
        headers.put(Exchange.FILE_NAME, fileReference);
        headers.put(Exchange.CONTENT_TYPE, attachmentFormData.file.contentType());

        producerTemplate.request(
                getEndpointUrl(CREATE_OPERATION),
                exchange -> {
                    exchange.getMessage().setHeaders(headers);
                    exchange.getMessage().setBody(new FileInputStream(attachmentFormData.file.uploadedFile().toFile()));
                });

        var attachment = new Attachment();
        attachment.reference = fileReference;
        attachment.name = attachmentFormData.file.fileName();
        attachment.description = attachmentFormData.description;
        attachment.size = attachmentFormData.file.size();
        attachment.contentType = attachmentFormData.file.contentType();

        return persist(attachment).await().indefinitely();
    }

    public GridFSDownloadStream retrieveFile(String fileReference) {
        var headers = new HashMap<String, Object>();
        headers.put(Exchange.FILE_NAME, fileReference);

        var result =  producerTemplate.request(getEndpointUrl(FIND_ONE_OPERATION),
                exchange -> {
                    exchange.getMessage().setHeaders(headers);
         });

        return result.getMessage().getBody(GridFSDownloadStream.class);
    }

    private String getEndpointUrl(String operation) {
        return String.format(ENDPOINT_URL + operation, DEFAULT_MONGO_CLIENT_NAME, database);
    }
}
