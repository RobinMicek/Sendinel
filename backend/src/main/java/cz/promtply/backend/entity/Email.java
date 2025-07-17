package cz.promtply.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "to")
    private String to;

    @Column(name = "track_code")
    private String trackCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template", nullable = false)
    private Template template;

    @Column(name = "template_variables")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> templateVariables;

    @Column(name = "priority", length = 25)
    private String priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by")
    private Sender sentBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by")
    private Client requestedBy;

    @ColumnDefault("now()")
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @OneToMany(mappedBy = "email")
    private Set<EmailStatus> emailStatuses = new LinkedHashSet<>();

}