package cz.promtply.api.controller;

import cz.promtply.api.entity.Client;
import cz.promtply.api.service.ClientService;
import cz.promtply.api.util.SecurityUtil;
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
