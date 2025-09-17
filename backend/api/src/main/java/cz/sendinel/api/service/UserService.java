package cz.sendinel.api.service;

import cz.sendinel.api.dto.user.UserCreateRequestDto;
import cz.sendinel.api.dto.user.UserUpdateRequestDto;
import cz.sendinel.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
    Page<User> getUsers(Pageable pageable, Specification<User> specification);
    User updateUser(UUID id, User user);
    User updateUserFromDto(UUID id, UserUpdateRequestDto userUpdateRequestDto, User updatedBy);
    void deleteUser(User user);
    void deleteUser(UUID id, User deletedBy);

    boolean hasTotp(UUID id);
    boolean hasTotpActivated(UUID id);

    boolean isOobe();
}
