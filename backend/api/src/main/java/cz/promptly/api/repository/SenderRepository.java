package cz.promptly.api.repository;

import cz.promptly.api.entity.Sender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SenderRepository extends JpaRepository<Sender, Integer> {
    Optional<Sender> findByIdAndDeletedOnIsNull(UUID id);
    List<Sender> findByDeletedOnIsNull();
    Page<Sender> findByDeletedOnIsNull(Pageable pageable);
}
