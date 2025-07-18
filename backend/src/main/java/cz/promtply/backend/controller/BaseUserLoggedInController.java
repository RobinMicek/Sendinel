package cz.promtply.backend.controller;

import cz.promtply.backend.entity.User;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseUserLoggedInController {

    @Autowired
    private UserService userService;

    protected User getLoggedInUser() {
        return userService.getUserById(SecurityUtil.getLoggedInUserId()).orElseThrow();
    }

}
