package slash.process.meapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.RxJavaCouchbaseOperations;
import org.springframework.data.couchbase.core.mapping.CouchbasePersistentEntity;
import org.springframework.data.couchbase.repository.query.CouchbaseEntityInformation;
import org.springframework.data.couchbase.repository.support.ReactiveN1qlCouchbaseRepository;
import org.springframework.data.repository.util.QueryExecutionConverters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * A custom implementation of {@code CouchbaseRepository}.
 */
public class CustomReactiveN1qlCouchbaseRepository<T, ID extends Serializable> extends ReactiveN1qlCouchbaseRepository<T, ID> {

    private final CouchbasePersistentEntity<?> persistentEntity;

    @Autowired
    private CouchbaseTemplate template;

    /**
     * Create a new Repository.
     *
     * @param metadata            the Metadata for the entity.
     * @param couchbaseOperations the reference to the template used.
     */
    public CustomReactiveN1qlCouchbaseRepository(CouchbaseEntityInformation<T, String> metadata, RxJavaCouchbaseOperations couchbaseOperations) {
        super(metadata, couchbaseOperations);
        persistentEntity = getCouchbaseOperations().getConverter().getMappingContext().getPersistentEntity(getEntityInformation().getJavaType());
        template = new CouchbaseTemplate(getCouchbaseOperations().getCouchbaseClusterInfo(), getCouchbaseOperations().getCouchbaseBucket());
        allowPageable();
    }

    // Temporary Hack to fix pageable
    @SuppressWarnings("unchecked")
    private void allowPageable() {
        try {
            Field allowed_pageable_types = QueryExecutionConverters.class.getDeclaredField("ALLOWED_PAGEABLE_TYPES");
            allowed_pageable_types.setAccessible(true);
            Set<Class<?>> ALLOWED_PAGEABLE_TYPES = (Set<Class<?>>) allowed_pageable_types.get(QueryExecutionConverters.class);
            ALLOWED_PAGEABLE_TYPES.add(Flux.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        return super.save(populateIdIfNecessary(entity));
    }

    /**
     * Add generated ID to entity if not already set.
     *
     * @param entity the entity to update.
     * @return entity with ID set.
     */
    private <S extends T> S populateIdIfNecessary(S entity) {
        if (getEntityInformation().getId(entity) != null) {
            return entity;
        }
        setId(entity, template.getGeneratedId(entity));
        return entity;
    }

    private <S extends T> void setId(S entity, String generatedId) {
        persistentEntity.getPropertyAccessor(entity).setProperty(persistentEntity.getIdProperty(), generatedId);
    }
}
