package cz.promtply.api.repository;

import cz.promtply.api.entity.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailStatusRepository extends JpaRepository<EmailStatus, Integer> {
    List<EmailStatus> findByEmailId(UUID id);

}
