package cz.promtply.backend.repository;

import cz.promtply.backend.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Optional<Template> findById(UUID id);
    boolean existsById(UUID id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Template t WHERE t.id = :id")
    boolean existsByIdIncludingDeleted(UUID id);
}
