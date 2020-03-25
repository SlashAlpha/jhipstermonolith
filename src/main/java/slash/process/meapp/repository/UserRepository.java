package slash.process.meapp.repository;

import slash.process.meapp.domain.User;


import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static slash.process.meapp.config.Constants.ID_DELIMITER;

/**
 * Spring Data Couchbase repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends ReactiveN1qlCouchbaseRepository<User, String> {

    Mono<User> findOneByEmailIgnoreCase(String email);

    default Mono<User> findOneByLogin(String login) {
        return findById(User.PREFIX + ID_DELIMITER + login);
    }



    Flux<User> findAllByLoginNot(Pageable pageable, String login);

    Mono<Long> countAllByLoginNot(String anonymousUser);
}
