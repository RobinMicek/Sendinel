package cz.promtply.api.service;

import cz.promtply.api.entity.User;
import cz.promtply.api.entity.UserTotp;

import java.util.UUID;

public interface UserTotpService {
    UserTotp getByUserId(UUID userId);
    void deleteTotp(User user);
    UserTotp createUserTotp(UserTotp userTotp);
    String generateAndCreateTotp(User user);
    void activateTotp(String code, User user);
    boolean verifyTotp(String code, User user);
}
