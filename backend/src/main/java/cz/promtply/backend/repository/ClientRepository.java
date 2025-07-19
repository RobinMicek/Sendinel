package cz.promtply.backend.repository;

import cz.promtply.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findById(UUID id);
}
