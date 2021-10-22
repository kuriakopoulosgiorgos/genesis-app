package test.utils;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.smallrye.mutiny.Uni;
import org.mockito.Mockito;

import java.util.List;

public class TestUtil {

    public static <T extends PanacheEntity> T entityWithId(Long id, T entity) {
        entity.id = id;
        return entity;
    }

    public static <T> void withFirstResultNullQuery(PanacheQuery<T> queryCall) {
        PanacheQuery<T> mockedQuery = Mockito.mock(PanacheQuery.class);

        Mockito.when(queryCall)
                .thenReturn(mockedQuery);

        Mockito.when(mockedQuery.firstResult()).thenReturn(Uni.createFrom().nullItem());
    }

    public static <T> void withFirstResultQuery(PanacheQuery<T> queryCall, T firstResult) {
        PanacheQuery<T> mockedQuery = Mockito.mock(PanacheQuery.class);

        Mockito.when(queryCall)
                .thenReturn(mockedQuery);

        Mockito.when(mockedQuery.firstResult()).thenReturn(Uni.createFrom().item(firstResult));
    }

    public static <T> void withListResultQuery(PanacheQuery<T> queryCall, List<T> listResult) {
        PanacheQuery<T> mockedQuery = Mockito.mock(PanacheQuery.class);

        Mockito.when(queryCall)
                .thenReturn(mockedQuery);

        Mockito.when(mockedQuery.list()).thenReturn(Uni.createFrom().item(listResult));
    }
}
