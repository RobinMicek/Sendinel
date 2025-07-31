package cz.promptly.api.entity;

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

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "app_settings")
public class AppSettings {
    @Id
    @Column(name = "id", nullable = false)
    private long id = 1L;

    @ColumnDefault("false")
    @Column(name = "track_opened_emails", nullable = false)
    private boolean trackOpenedEmails;

    @ColumnDefault("true")
    @Column(name = "allow_template_imports", nullable = false)
    private boolean allowTemplateImports;

    @ColumnDefault("true")
    @Column(name = "display_new_version_alert", nullable = false)
    private boolean displayNewVersionAlert;

    @ColumnDefault("true")
    @Column(name = "use_gravatar", nullable = false)
    private boolean useGravatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @ColumnDefault("now()")
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

}