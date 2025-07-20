package cz.promtply.backend.controller;

import cz.promtply.backend.entity.Client;
import cz.promtply.backend.service.ClientService;
import cz.promtply.backend.util.SecurityUtil;
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
