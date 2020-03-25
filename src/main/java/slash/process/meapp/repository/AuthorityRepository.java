package slash.process.meapp.repository;

import slash.process.meapp.domain.Authority;

/**
 * Spring Data Couchbase repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends ReactiveN1qlCouchbaseRepository<Authority, String> {
}
