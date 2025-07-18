package cz.promtply.backend.entity;

import cz.promtply.backend.enums.TemplateType;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "templates")
@SQLDelete(sql = "UPDATE templates SET deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_on IS NULL")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text_raw", length = Integer.MAX_VALUE)
    private String textRaw;

    @Column(name = "html_raw", length = Integer.MAX_VALUE)
    private String htmlRaw;

    @Column(name = "markdown_raw", length = Integer.MAX_VALUE)
    private String markdownRaw;

    @Column(name = "prefered_template_type", length = 25)
    private TemplateType preferedTemplateType;

    @Column(name = "schema")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> schema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @ColumnDefault("now()")
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @ColumnDefault("now()")
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    @Column(name = "deleted_on")
    private Instant deletedOn;

    @OneToMany(mappedBy = "template")
    private Set<Email> emails = new LinkedHashSet<>();

}