package cz.promtply.api.repository;

import cz.promtply.api.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SenderRepository extends JpaRepository<Sender, Integer> {
    Optional<Sender> findById(UUID id);
}
