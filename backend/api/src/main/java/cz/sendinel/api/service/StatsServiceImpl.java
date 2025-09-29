package cz.sendinel.api.service;

import cz.sendinel.api.dto.stats.StatsRequestDto;
import cz.sendinel.api.dto.stats.StatsResponseDto;
import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.EmailStatus;
import cz.sendinel.api.repository.EmailRepository;
import cz.sendinel.api.repository.EmailStatusRepository;
import cz.sendinel.api.repository.StatsRepository;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EmailRepository emailRepository;
    private final StatsRepository statsRepository;

    public StatsResponseDto getStats(StatsRequestDto filter) {
        LocalDateTime[] range = resolveDateRange(filter);
        LocalDateTime start = range[0];
        LocalDateTime end = range[1];

        // RSQL spec for Email
        Specification<Email> emailSpec = (root, query, cb) -> cb.between(root.get("createdOn"), start, end);

        // RSQL spec for EmailStatus
        Specification<EmailStatus> statusSpec = (root, query, cb) -> cb.between(root.get("createdOn"), start, end);

        // Total sent
        long totalSent = emailRepository.count(emailSpec);

        // Unique recipients
        long uniqueRecipients = statsRepository.countDistinctToAddress(emailSpec);

        // Status counts
        Map<EmailStatusesEnum, Long> statusCounts = statsRepository.countByStatus(statusSpec);
        long opened = statusCounts.getOrDefault(EmailStatusesEnum.OPENED, 0L);
        long bounced = statusCounts.getOrDefault(EmailStatusesEnum.BOUNCED, 0L);
        long failed = statusCounts.getOrDefault(EmailStatusesEnum.FAILED, 0L);


        // Daily totals
        List<Object[]> daily = statsRepository.countSentDaily(emailSpec, start, end);
        Map<LocalDate, Long> dailyCounts = daily.stream().collect(Collectors.toMap(
                r -> ((java.sql.Date) r[0]).toLocalDate(),
                r -> ((Number) r[1]).longValue()
        ));

        // Response
        StatsResponseDto response = new StatsResponseDto();
        response.setTotalSent(totalSent);
        response.setTotalSentDaily(dailyCounts);
        response.setOpenRate(totalSent == 0 ? 0 : (opened * 100.0 / totalSent));
        response.setBouncedRate(totalSent == 0 ? 0 : (bounced * 100.0 / totalSent));
        response.setFailedRate(totalSent == 0 ? 0 : (failed * 100.0 / totalSent));
        response.setUniqueRecipients(uniqueRecipients);
        return response;
    }

    private Specification<Email> andDateRange(Specification<Email> spec, LocalDateTime start, LocalDateTime end) {
        return spec.and((root, query, cb) -> cb.between(root.get("createdOn"), start, end));
    }

    private Specification<EmailStatus> andDateRangeForStatus(Specification<EmailStatus> spec, LocalDateTime start, LocalDateTime end) {
        return spec.and((root, query, cb) -> cb.between(root.get("createdOn"), start, end));
    }

    private LocalDateTime[] resolveDateRange(StatsRequestDto filter) {
        return new LocalDateTime[]{
            filter.getStartDate().atStartOfDay(),
            filter.getEndDate().atTime(23, 59, 59)
        };
    }
}
