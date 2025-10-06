package cz.sendinel.api.repository;

import cz.sendinel.api.entity.TemplateTag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TemplateTagRepository extends JpaRepository<TemplateTag, UUID> {
    Optional<TemplateTag> findByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM TemplateTag t WHERE t.templates IS EMPTY")
    void deleteAllOrphanTags();
}