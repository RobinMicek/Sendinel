package cz.promtply.backend.service;

import cz.promtply.backend.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(UUID id, User user);
    void deleteUser(User user);
    void deleteUser(UUID id);
}
