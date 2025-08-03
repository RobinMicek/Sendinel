package cz.sendinel.api.service;

import cz.sendinel.api.entity.User;
import cz.sendinel.api.entity.UserTotp;

import java.util.UUID;

public interface UserTotpService {
    UserTotp getByUserId(UUID userId);
    void deleteTotp(User user);
    UserTotp createUserTotp(UserTotp userTotp);
    String generateAndCreateTotp(User user);
    void activateTotp(String code, User user);
    boolean verifyTotp(String code, User user);
}
