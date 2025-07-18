package cz.promtply.backend.service;

import cz.promtply.backend.entity.User;
import cz.promtply.backend.entity.UserTotp;
import cz.promtply.backend.exceptions.AlreadyExistsException;
import cz.promtply.backend.exceptions.ResourceNotFoundException;
import cz.promtply.backend.repository.UserRepository;
import cz.promtply.backend.repository.UserTotpRepository;
import cz.promtply.backend.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTotpServiceImpl implements UserTotpService {

    private final UserTotpRepository userTotpRepository;
    private final UserRepository userRepository;
    private final TotpUtil totpUtil;

    @Override
    public UserTotp getByUserId(UUID userId) {
        return userTotpRepository.findByUserId(userId);
    }

    @Override
    public void deleteTotp(User user) {
        if (!userTotpRepository.existsByUserId(user.getId())) {
            throw new ResourceNotFoundException("TOTP not found for user id " + user.getId());
        }

        // Remove association with user
        user.setTotp(null);
        userRepository.save(user);

        UserTotp userTotp = userTotpRepository.findByUserId(user.getId());
        userTotpRepository.delete(userTotp);
    }

    @Override
    public UserTotp createUserTotp(UserTotp userTotp) {
        if (userTotpRepository.existsByUserId(userTotp.getUser().getId())) {
            throw new AlreadyExistsException("TOTP already exists for user id " + userTotp.getUser().getId());
        }

        userTotp.setCreatedOn(Instant.now());
        userTotp.setUpdatedOn(Instant.now());

        return userTotpRepository.save(userTotp);
    }

    @Override
    public UserTotp generateTotp(User user) {
        String secret = totpUtil.generateKey();

        UserTotp userTotp = new UserTotp();
        userTotp.setUser(user);
        userTotp.setSecret(secret);

        return createUserTotp(userTotp);
    }

    @Override
    public void activateTotp(String code, User user) {
        if (!userTotpRepository.existsByUserId(user.getId())) {
            throw new ResourceNotFoundException("TOTP not found for user id " + user.getId());
        }

        if (user.getTotp().isActivated()) {
            throw new AlreadyExistsException("TOTP already activated for user id " + user.getId());
        }

        boolean valid = TotpUtil.verifyTotp(user.getTotp().getSecret(), Integer.parseInt(code));
        if (!valid) {
            throw new SecurityException("Invalid TOTP code");
        }

        UserTotp userTotp = getByUserId(user.getId());
        userTotp.setActivated(true);
        userTotp.setUpdatedOn(Instant.now());

        userTotpRepository.save(userTotp);
    }
}
