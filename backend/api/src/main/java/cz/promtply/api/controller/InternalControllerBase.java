package cz.promtply.api.controller;

import cz.promtply.api.entity.User;
import cz.promtply.api.service.UserService;
import cz.promtply.api.util.SecurityUtil;
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
