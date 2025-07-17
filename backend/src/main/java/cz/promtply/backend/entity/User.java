package cz.promtply.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
@Table(name = "users")
@SQLDelete(sql = "UPDATE user SET deleted_on = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_on IS NULL")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "firstname", length = 25)
    private String firstname;

    @Column(name = "lastname", length = 25)
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash", length = 128)
    private String passwordHash;

    @Column(name = "role", length = 25)
    private String role;

    @ColumnDefault("now()")
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @ColumnDefault("now()")
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    @Column(name = "deleted_on")
    private Instant deletedOn;

    @OneToOne(mappedBy = "user")
    private UserTotp totp;

}