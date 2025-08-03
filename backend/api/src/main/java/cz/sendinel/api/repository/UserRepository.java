package cz.sendinel.api.repository;

import cz.sendinel.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndDeletedOnIsNull(String email);
    Optional<User> findByIdAndDeletedOnIsNull(UUID id);
    List<User> findAllByDeletedOnIsNull();
    Page<User> findAllByDeletedOnIsNull(Pageable pageable);
}
