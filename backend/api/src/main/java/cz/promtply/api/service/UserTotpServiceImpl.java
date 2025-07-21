package cz.promtply.api.service;

import cz.promtply.api.entity.User;
import cz.promtply.api.entity.UserTotp;
import cz.promtply.api.exceptions.AlreadyExistsException;
import cz.promtply.api.exceptions.ResourceNotFoundException;
import cz.promtply.api.repository.UserRepository;
import cz.promtply.api.repository.UserTotpRepository;
import cz.promtply.api.util.TotpUtil;
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
    public String generateAndCreateTotp(User user) {
        String secret = totpUtil.generateKey();

        UserTotp userTotp = new UserTotp();
        userTotp.setUser(user);

        // Encrypt the secret
        String encryptedSecret;
        try {
            encryptedSecret = totpUtil.encrypt(secret);
        } catch (Exception e) {
            throw new SecurityException("Failed to encrypt TOTP secret", e);
        }

        userTotp.setSecret(encryptedSecret);

        createUserTotp(userTotp);

        return secret;
    }

    @Override
    public void activateTotp(String code, User user) {
        if (!userTotpRepository.existsByUserId(user.getId())) {
            throw new ResourceNotFoundException("TOTP not found for user id " + user.getId());
        }

        if (user.getTotp().isActivated()) {
            throw new AlreadyExistsException("TOTP already activated for user id " + user.getId());
        }

        boolean valid = verifyTotp(code, user);
        if (!valid) {
            throw new SecurityException("Invalid TOTP code");
        }

        UserTotp userTotp = getByUserId(user.getId());
        userTotp.setActivated(true);
        userTotp.setUpdatedOn(Instant.now());

        userTotpRepository.save(userTotp);
    }

    @Override
    public boolean verifyTotp(String code, User user) {
        if (!userTotpRepository.existsByUserId(user.getId())) {
            throw new ResourceNotFoundException("TOTP not found for user id " + user.getId());
        }

        String decryptedSecret;
        try {
            decryptedSecret = totpUtil.decrypt(user.getTotp().getSecret());
        } catch (Exception e) {
            throw new SecurityException("Failed to decrypt TOTP secret", e);
        }

        return TotpUtil.verifyTotp(decryptedSecret, Integer.parseInt(code));
    }
}
