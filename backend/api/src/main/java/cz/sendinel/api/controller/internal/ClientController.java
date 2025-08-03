package cz.sendinel.api.controller.internal;

import cz.sendinel.shared.config.Constants;
import cz.sendinel.api.controller.InternalControllerBase;
import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.client.ClientRequestDto;
import cz.sendinel.api.dto.client.ClientResponseDto;
import cz.sendinel.api.dto.client.token.ClientTokenRequestDto;
import cz.sendinel.api.dto.client.token.ClientTokenResponseDto;
import cz.sendinel.api.dto.client.token.ClientTokenValueResponseDto;
import cz.sendinel.api.entity.Client;
import cz.sendinel.api.entity.ClientToken;
import cz.sendinel.api.service.ClientService;
import cz.sendinel.api.service.ClientTokenService;
import cz.sendinel.api.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/client")
@RequiredArgsConstructor
public class ClientController extends InternalControllerBase {

    private final ClientService clientService;
    private final ClientTokenService clientTokenService;

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENTS_READ')")
    public ResponseEntity<PageResponseDto<ClientResponseDto>> getClients(Pageable pageable) {
        Page<Client> clientPage = clientService.getClients(pageable);

        // Map Page<Client> to Page<ClientResponseDto>
        Page<ClientResponseDto> dtoPage = clientPage.map(client -> MapperUtil.toDto(client, ClientResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_READ')")
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable UUID id) {
        Client client = clientService.getClientById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENTS_CREATE')")
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto clientRequestDto) {
        Client client = clientService.createClientFromDto(clientRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_UPDATE')")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientRequestDto clientRequestDto) {
        Client client = clientService.updateClientFromDto(id, clientRequestDto, getLoggedInUser());

        return ResponseEntity.ok().body(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_DELETE')")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/token")
    @PreAuthorize("hasAuthority('CLIENT_TOKENS_READ')")
    public ResponseEntity<List<ClientTokenResponseDto>> getClientTokens(@PathVariable UUID id) {
        List<ClientToken> clientTokenList = clientTokenService.getAllClientTokens(id);

        // Map List<ClientToken> to List<ClientTokenResponseDto> and set isExpired
        List<ClientTokenResponseDto> response = clientTokenList.stream()
                .map(clientToken -> {
                    ClientTokenResponseDto dto = MapperUtil.toDto(clientToken, ClientTokenResponseDto.class);
                    dto.setIsExpired(clientTokenService.isExpired(clientToken));
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/token")
    @PreAuthorize("hasAuthority('CLIENT_TOKENS_CREATE')")
    public ResponseEntity<ClientTokenValueResponseDto> createClientToken(@PathVariable UUID id, @Valid @RequestBody ClientTokenRequestDto clientTokenRequestDto) {
        String token = clientTokenService.generateAndCreateTokenFromDto(id, clientTokenRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ClientTokenValueResponseDto(token));
    }

    @DeleteMapping("/{clientId}/token/{tokenId}")
    @PreAuthorize("hasAuthority('CLIENT_TOKENS_DELETE')")
    public ResponseEntity<Void> deleteClientToken(@PathVariable UUID clientId, @PathVariable UUID tokenId) {
        clientTokenService.deleteToken(tokenId, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
