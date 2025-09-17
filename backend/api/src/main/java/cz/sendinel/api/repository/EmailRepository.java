package cz.sendinel.api.repository;

import cz.sendinel.api.entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, Integer> {
    Optional<Email> findById(UUID id);
    Optional<Email> findByTrackCode(String trackCode);
    Page<Email> findAll(Specification<Email> spec, Pageable pageable);
}
