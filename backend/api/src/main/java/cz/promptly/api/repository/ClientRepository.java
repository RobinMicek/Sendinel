package cz.promptly.api.repository;

import cz.promptly.api.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByIdAndDeletedOnIsNull(UUID id);
    List<Client> findByDeletedOnIsNull();
    Page<Client> findByDeletedOnIsNull(Pageable pageable);
}
