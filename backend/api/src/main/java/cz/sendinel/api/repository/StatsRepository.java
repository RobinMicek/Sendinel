package cz.sendinel.api.repository;

import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.EmailStatus;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsRepository {
    long countDistinctToAddress(Specification<Email> spec);
    Map<EmailStatusesEnum, Long> countByStatus(Specification<EmailStatus> spec);
    List<Object[]> countSentDaily(Specification<Email> spec, LocalDateTime start, LocalDateTime end);
}
