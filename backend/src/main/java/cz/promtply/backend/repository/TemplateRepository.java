package cz.promtply.backend.repository;

import cz.promtply.backend.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Optional<Template> findById(UUID id);
}
