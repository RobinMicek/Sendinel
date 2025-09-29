package cz.sendinel.api.repository;

import cz.sendinel.api.entity.Email;
import cz.sendinel.api.entity.EmailStatus;
import cz.sendinel.shared.enums.EmailStatusesEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final EntityManager em;

    @Override
    public long countDistinctToAddress(Specification<Email> spec) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Email> root = query.from(Email.class);
        query.select(cb.countDistinct(root.get("toAddress")));
        if (spec != null) {
            query.where(spec.toPredicate(root, query, cb));
        }
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public Map<EmailStatusesEnum, Long> countByStatus(Specification<EmailStatus> spec) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<EmailStatus> root = query.from(EmailStatus.class);
        query.multiselect(root.get("status"), cb.count(root));
        if (spec != null) {
            query.where(spec.toPredicate(root, query, cb));
        }
        query.groupBy(root.get("status"));
        List<Object[]> results = em.createQuery(query).getResultList();
        return results.stream().collect(Collectors.toMap(
                r -> (EmailStatusesEnum) r[0],
                r -> (Long) r[1]
        ));
    }

    public List<Object[]> countSentDaily(Specification<Email> spec, LocalDateTime start, LocalDateTime end) {
        String sql = """
            WITH RECURSIVE date_series (d) AS (
              SELECT DATE(:start)
              UNION ALL
              SELECT (d + INTERVAL '1' DAY)::date
              FROM date_series
              WHERE d < DATE(:end)
            )
            SELECT
                ds.d AS day,
                COUNT(e.id) AS count
            FROM
                date_series ds
            LEFT JOIN
                emails e ON DATE(e.created_on) = ds.d
            GROUP BY
                ds.d
            ORDER BY
                ds.d
        """;

        return em.createNativeQuery(sql)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
