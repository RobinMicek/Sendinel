package cz.promptly.api.repository;

import cz.promptly.api.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SenderRepository extends JpaRepository<Sender, Integer> {
    Optional<Sender> findById(UUID id);
}
