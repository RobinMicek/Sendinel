package cz.promtply.backend.repository;

import cz.promtply.backend.entity.ClientToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientTokenRepository extends JpaRepository<ClientToken, Integer> {
    Optional<ClientToken> findById(UUID id);
    Optional<ClientToken> findFirstByClientId(UUID id);
    List<ClientToken> findByClientId(UUID id);
    Optional<ClientToken> findByTokenFingerprint(String tokenFingerprint);
}
