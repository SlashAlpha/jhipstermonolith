package slash.process.meapp.repository;

import slash.process.meapp.domain.PersistentAuditEvent;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Spring Data Couchbase repository for the {@link PersistentAuditEvent} entity.
 */
public interface PersistenceAuditEventRepository extends ReactiveN1qlCouchbaseRepository<PersistentAuditEvent, String> {

    Flux<PersistentAuditEvent> findByPrincipal(String principal);

    Flux<PersistentAuditEvent> findAllByAuditEventDateBetween(Instant fromDate, Instant toDate, Pageable pageable);

    Flux<PersistentAuditEvent> findByAuditEventDateBefore(Instant before);

    Flux<PersistentAuditEvent> findAllBy(Pageable pageable);

    Mono<Long> countByAuditEventDateBetween(Instant fromDate, Instant toDate);
}
