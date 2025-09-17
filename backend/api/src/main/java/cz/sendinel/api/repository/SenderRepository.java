package cz.sendinel.api.repository;

import cz.sendinel.api.entity.Sender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SenderRepository extends JpaRepository<Sender, Integer> {
    Optional<Sender> findByIdAndDeletedOnIsNull(UUID id);
    List<Sender> findAllByDeletedOnIsNull();
    Page<Sender> findAllByDeletedOnIsNull(Pageable pageable);
    Page<Sender> findAll(Specification<Sender> spec, Pageable pageable);
}
