package cz.promtply.backend.repository;

import cz.promtply.backend.entity.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailStatusRepository extends JpaRepository<EmailStatus, Integer> {
    List<EmailStatus> findByEmailId(UUID id);

}
