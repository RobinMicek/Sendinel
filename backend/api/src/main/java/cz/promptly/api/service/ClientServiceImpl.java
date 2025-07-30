package cz.promptly.api.service;

import cz.promptly.api.dto.client.ClientRequestDto;
import cz.promptly.api.entity.Client;
import cz.promptly.api.entity.User;
import cz.promptly.api.exceptions.ResourceNotFoundException;
import cz.promptly.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final SenderService senderService;

    @Override
    public Client createClient(Client client) {
        client.setCreatedOn(Instant.now());
        client.setUpdatedOn(Instant.now());

        return clientRepository.save(client);
    }

    @Override
    public Client createClientFromDto(ClientRequestDto clientRequestDto, User createdBy) {
        Client client = new Client();
        client.setName(clientRequestDto.getName());
        client.setDescription(clientRequestDto.getDescription());

        client.setSender(senderService.getSenderById(clientRequestDto.getSenderId()).orElse(null));

        client.setCreatedBy(createdBy);
        client.setUpdatedBy(createdBy);

        return createClient(client);
    }

    @Override
    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findByIdAndDeletedOnIsNull(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findByDeletedOnIsNull();
    }

    @Override
    public Page<Client> getClients(Pageable pageable) {
        return clientRepository.findByDeletedOnIsNull(pageable);
    }

    @Override
    public Client updateClient(UUID id, Client client) {
        Client existingClient = clientRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(
                () -> new ResourceNotFoundException("Client not found with id " + id)
        );

        existingClient.setName(client.getName());
        existingClient.setDescription(client.getDescription());
        existingClient.setSender(client.getSender());

        existingClient.setUpdatedBy(client.getUpdatedBy());
        existingClient.setUpdatedOn(Instant.now());

        return clientRepository.save(existingClient);
    }

    @Override
    public Client updateClientFromDto(UUID id, ClientRequestDto clientRequestDto, User updatedBy) {
        Client client = new Client();
        client.setName(clientRequestDto.getName());
        client.setDescription(clientRequestDto.getDescription());

        // Set sender to null if intended, but throw exception when senderId is present but sender not exist
        if (clientRequestDto.getSenderId() != null) {
            client.setSender(senderService.getSenderById(clientRequestDto.getSenderId()).orElseThrow(
                    () -> new ResourceNotFoundException("Sender not found with id " + id)
            ));
        } else {
            client.setSender(null);
        }

        client.setUpdatedBy(updatedBy);

        return updateClient(id, client);
    }

    @Override
    public void deleteClient(Client client) {
        client.setSender(null);
        clientRepository.delete(client);
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = clientRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        deleteClient(client);
    }

    @Override
    public void deleteClient(UUID id, User deletedBy) {
        Client client = clientRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        client.setUpdatedBy(deletedBy);

        deleteClient(client);
    }
}
