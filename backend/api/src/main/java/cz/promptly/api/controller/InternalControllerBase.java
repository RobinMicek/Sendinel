package cz.promptly.api.controller;

import cz.promptly.api.entity.User;
import cz.promptly.api.service.UserService;
import cz.promptly.api.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class InternalControllerBase {

    @Autowired
    private UserService userService;

    protected User getLoggedInUser() {
        return userService.getUserById(SecurityUtil.getLoggedInPrincipalId()).orElseThrow(
                () -> new RuntimeException("Could not find user with id " + SecurityUtil.getLoggedInPrincipalId())
        );
    }

}
