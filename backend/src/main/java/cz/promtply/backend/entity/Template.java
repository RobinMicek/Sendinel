package cz.promtply.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "templates")
@SQLDelete(sql = "UPDATE templates SET deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_on IS NULL")
public class Template {
    // !!! DOES NOT AUTOMATICALLY GENERATE IDs, NEED TO CALL .setId() ON CREATION !!! - CURRENTLY GETS SET IN UserService.createTemplate(Template template)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text_raw", columnDefinition = "TEXT")
    private String textRaw;

    @Column(name = "html_raw", columnDefinition = "TEXT")
    private String htmlRaw;

    @Column(name = "schema", columnDefinition = "TEXT")
    private String schema;

    @Column(name = "reply_to")
    private String replyTo;

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
}