package cz.promtply.backend.service;

import cz.promtply.backend.entity.User;
import cz.promtply.backend.entity.UserTotp;

import java.util.UUID;

public interface UserTotpService {
    UserTotp getByUserId(UUID userId);

    void deleteTotp(User user);

    UserTotp createUserTotp(UserTotp userTotp);

    UserTotp generateTotp(User user);
    void activateTotp(String code, User user);
}
