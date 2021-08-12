package test;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import org.bson.types.ObjectId;

public class TestUtil {

    public static <T extends ReactivePanacheMongoEntity> T entityWithId(String id, T entity) {
        entity.id = new ObjectId(id);
        return entity;
    }
}
