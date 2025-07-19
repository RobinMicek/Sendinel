package cz.promtply.backend.controller;

import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.client.ClientRequestDto;
import cz.promtply.backend.dto.client.ClientResponseDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.service.ClientService;
import cz.promtply.backend.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController extends BaseUserLoggedInController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<PageResponseDto<ClientResponseDto>> getSenders(Pageable pageable) {
        Page<Client> clientPage = clientService.getClients(pageable);

        // Map Page<Client> to Page<ClientResponseDto>
        Page<ClientResponseDto> dtoPage = clientPage.map(client -> MapperUtil.toDto(client, ClientResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable UUID id) {
        Client client = clientService.getClientById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto clientRequestDto) {
        Client client = clientService.createClientFromDto(clientRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientRequestDto clientRequestDto) {
        Client client = clientService.updateClientFromDto(id, clientRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(client, ClientResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
