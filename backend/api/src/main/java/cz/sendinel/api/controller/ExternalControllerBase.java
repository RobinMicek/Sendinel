package cz.sendinel.api.controller;

import cz.sendinel.api.entity.Client;
import cz.sendinel.api.service.ClientService;
import cz.sendinel.api.util.SecurityUtil;
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
