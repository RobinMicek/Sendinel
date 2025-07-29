package cz.promptly.api.repository;

import cz.promptly.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findById(UUID id);
}
