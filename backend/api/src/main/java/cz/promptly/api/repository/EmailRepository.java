package cz.promptly.api.repository;

import cz.promptly.api.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    Optional<Email> findById(UUID id);
    Optional<Email> findByTrackCode(String trackCode);
}
