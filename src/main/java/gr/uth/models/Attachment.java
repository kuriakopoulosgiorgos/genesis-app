package gr.uth.models;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

@MongoEntity
public class Attachment extends ReactivePanacheMongoEntity {

    public String reference;
    public String name;
    public String description;
    public Long size;
    public String contentType;
}
