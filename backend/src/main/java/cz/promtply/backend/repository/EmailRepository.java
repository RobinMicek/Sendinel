package cz.promtply.backend.repository;

import cz.promtply.backend.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    Optional<Email> findById(UUID id);
    Optional<Email> findByTrackCode(String trackCode);
}
