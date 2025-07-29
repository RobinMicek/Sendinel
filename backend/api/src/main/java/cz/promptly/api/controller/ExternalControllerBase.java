package cz.promptly.api.controller;

import cz.promptly.api.entity.Client;
import cz.promptly.api.service.ClientService;
import cz.promptly.api.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ExternalControllerBase {

    @Autowired
    private ClientService clientService;

    protected Client getLoggedInClient() {
        return clientService.getClientById(SecurityUtil.getLoggedInPrincipalId()).orElseThrow(
                () -> new RuntimeException("Could not find client with id " + SecurityUtil.getLoggedInPrincipalId())
        );
    }
}
