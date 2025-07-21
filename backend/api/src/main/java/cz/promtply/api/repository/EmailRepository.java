package cz.promtply.api.repository;

import cz.promtply.api.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    Optional<Email> findById(UUID id);
    Optional<Email> findByTrackCode(String trackCode);
}
