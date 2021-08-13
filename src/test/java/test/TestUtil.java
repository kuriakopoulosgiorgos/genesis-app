package test;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

public class TestUtil {

    public static <T extends PanacheEntity> T entityWithId(Long id, T entity) {
        entity.id = id;
        return entity;
    }
}
