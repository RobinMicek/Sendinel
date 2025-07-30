package cz.promptly.api.repository;

import cz.promptly.api.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Optional<Template> findByIdAndDeletedOnIsNull(UUID id);
    boolean existsByIdAndDeletedOnIsNull(UUID id);
    boolean existsById(UUID id);
    List<Template> findAllByDeletedOnIsNull();
    Page<Template> findAllByDeletedOnIsNull(Pageable pageable);
}
