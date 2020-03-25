package slash.process.meapp.repository;

import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * Couchbase specific {@link org.springframework.data.repository.Repository} interface uses N1QL for all requests.
 */
@NoRepositoryBean
public interface ReactiveN1qlCouchbaseRepository<T, ID extends Serializable> extends ReactiveCouchbaseSortingRepository<T, ID> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    Flux<T> findAll();

    @Query("SELECT count(*) FROM #{#n1ql.bucket} WHERE #{#n1ql.filter}")
    Mono<Long> count();

    @Query("DELETE FROM #{#n1ql.bucket} WHERE #{#n1ql.filter} returning #{#n1ql.fields}")
    Flux<T> removeAll();

    default Mono<Void> deleteAll() {
        return removeAll().then();
    }
}
