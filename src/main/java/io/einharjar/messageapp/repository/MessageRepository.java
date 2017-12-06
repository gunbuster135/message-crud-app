package io.einharjar.messageapp.repository;

import io.einharjar.messageapp.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository, @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/">Spring Data Documentation</a> for
 * syntax when creating method signatures
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
    Optional<Message> findById(Long id);
}
