package cz.promtply.backend.service;

import cz.promtply.backend.entity.User;
import cz.promtply.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UUID id, User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        deleteUser(user);
    }
}
