package cz.sendinel.api.controller;

import cz.sendinel.api.entity.User;
import cz.sendinel.api.service.UserService;
import cz.sendinel.api.util.SecurityUtil;
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
