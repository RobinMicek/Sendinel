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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "senders")
@SQLDelete(sql = "UPDATE sender SET deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_on IS NULL")
public class Sender {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "type", length = 25)
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "configuration", length = Integer.MAX_VALUE)
    private String configuration;

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

    @OneToMany(mappedBy = "sentBy")
    private Set<Email> emails = new LinkedHashSet<>();

}