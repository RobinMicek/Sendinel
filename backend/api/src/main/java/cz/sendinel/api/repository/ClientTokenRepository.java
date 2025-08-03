package cz.sendinel.api.repository;

import cz.sendinel.api.entity.ClientToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientTokenRepository extends JpaRepository<ClientToken, Integer> {
    Optional<ClientToken> findByIdAndDeletedOnIsNull(UUID id);
    Optional<ClientToken> findFirstByClientIdAndDeletedOnIsNull(UUID id);
    List<ClientToken> findByClientIdAndDeletedOnIsNull(UUID id);
}
