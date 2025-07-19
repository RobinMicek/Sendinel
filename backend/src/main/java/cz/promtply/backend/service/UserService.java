package cz.promtply.backend.service;

import cz.promtply.backend.dto.user.UserCreateRequestDto;
import cz.promtply.backend.dto.user.UserUpdateRequestDto;
import cz.promtply.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User createUserFromDto(UserCreateRequestDto userCreateRequestDto, User createdBy);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    Page<User> getUsers(Pageable pageable);
    User updateUser(UUID id, User user);
    User updateUserFromDto(UUID id, UserUpdateRequestDto userUpdateRequestDto, User updatedBy);
    void deleteUser(User user);
    void deleteUser(UUID id, User deletedBy);

    boolean hasTotp(UUID id);

    boolean hasTotpActivated(UUID id);
}
