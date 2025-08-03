package cz.sendinel.api.repository;

import cz.sendinel.api.entity.UserTotp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTotpRepository extends JpaRepository<UserTotp, UUID> {
    UserTotp findByUserId(UUID userId);
    boolean existsByUserId(UUID userName);
}