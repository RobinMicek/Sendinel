package cz.promtply.api.service;

import cz.promtply.api.dto.client.ClientRequestDto;
import cz.promtply.api.entity.Client;
import cz.promtply.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {
    Client createClient(Client client);
    Client createClientFromDto(ClientRequestDto clientRequestDto, User createdBy);
    Optional<Client> getClientById(UUID id);
    List<Client> getAllClients();
    Page<Client> getClients(Pageable pageable);
    Client updateClient(UUID id, Client client);
    Client updateClientFromDto(UUID id, ClientRequestDto clientRequestDto, User updatedBy);
    void deleteClient(Client client);
    void deleteClient(UUID id);
    void deleteClient(UUID id, User deletedBy);


}
