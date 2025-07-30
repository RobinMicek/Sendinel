package cz.promptly.api.service;

import cz.promptly.api.dto.user.UserCreateRequestDto;
import cz.promptly.api.dto.user.UserUpdateRequestDto;
import cz.promptly.api.entity.User;
import cz.promptly.api.exceptions.ResourceNotFoundException;
import cz.promptly.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setCreatedOn(Instant.now());
        user.setUpdatedOn(Instant.now());

        return userRepository.save(user);
    }

    @Override
    public User createUserFromDto(UserCreateRequestDto userCreateRequestDto, User createdBy) {
        User user = new User();
        user.setFirstname(userCreateRequestDto.getFirstname());
        user.setLastname(userCreateRequestDto.getLastname());
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setRole(userCreateRequestDto.getRole());

        user.setCreatedBy(createdBy);
        user.setUpdatedBy(createdBy);

        return createUser(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findByIdAndDeletedOnIsNull(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedOnIsNull(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findByDeletedOnIsNull();
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findByDeletedOnIsNull(pageable);
    }

    @Override
    public User updateUser(UUID id, User user) {
        User existingUser = userRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + id)
        );

        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        existingUser.setUpdatedBy(user.getUpdatedBy());
        existingUser.setUpdatedOn(Instant.now());

        return userRepository.save(existingUser);
    }

    @Override
    public User updateUserFromDto(UUID id, UserUpdateRequestDto userUpdateRequestDto, User updatedBy) {
        User user = new User();
        user.setFirstname(userUpdateRequestDto.getFirstname());
        user.setLastname(userUpdateRequestDto.getLastname());
        user.setEmail(userUpdateRequestDto.getEmail());
        user.setRole(userUpdateRequestDto.getRole());

        user.setUpdatedBy(updatedBy);

        return updateUser(id, user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUser(UUID id, User deletedBy) {
        User user = userRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        user.setUpdatedBy(deletedBy);

        deleteUser(user);
    }

    @Override
    public boolean hasTotp(UUID id) {
        User user = userRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        return user.getTotp() != null;
    }

    @Override
    public boolean hasTotpActivated(UUID id) {
        User user = userRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        return user.getTotp() != null && user.getTotp().isActivated();
    }

    @Override
    public boolean isOobe() {
        return userRepository.count() == 0;
    }
}
