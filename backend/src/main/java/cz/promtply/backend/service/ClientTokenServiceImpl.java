package cz.promtply.backend.service;

import cz.promtply.backend.dto.client.token.ClientTokenRequestDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.ClientToken;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.exceptions.ResourceNotFoundException;
import cz.promtply.backend.repository.ClientTokenRepository;
import cz.promtply.backend.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientTokenServiceImpl implements ClientTokenService {

    private final ClientTokenRepository clientTokenRepository;
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<ClientToken> getByClientId(UUID clientId) {
        return clientTokenRepository.findFirstByClientId(clientId);
    }

    @Override
    public List<ClientToken> getAllClientTokens(Client client) {
        return clientTokenRepository.findByClientId(client.getId());
    }

    @Override
    public List<ClientToken> getAllClientTokens(UUID clientId) {
        return clientTokenRepository.findByClientId(clientId);
    }

    @Override
    public ClientToken createClientToken(ClientToken clientToken) {
        clientToken.setCreatedOn(Instant.now());

        return clientTokenRepository.save(clientToken);
    }

    @Override
    public String generateAndCreateTokenFromDto(UUID clientId, ClientTokenRequestDto clientTokenRequestDto, User createdBy) {
        Client client = clientService.getClientById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Client not found with id " + clientId)
        );

        String token = TokenUtil.generateToken();
        String tokenHash = passwordEncoder.encode(token);

        ClientToken clientToken = new ClientToken();
        clientToken.setName(clientTokenRequestDto.getName());
        clientToken.setDescription(clientTokenRequestDto.getDescription());
        clientToken.setExpiration(clientTokenRequestDto.getExpiration());
        clientToken.setTokenHash(tokenHash);
        clientToken.setClient(client);
        clientToken.setCreatedBy(createdBy);

        createClientToken(clientToken);

        return token;
    }

    @Override
    public String generateAndCreateTokenFromDto(Client client, ClientTokenRequestDto clientTokenRequestDto, User createdBy) {
        return generateAndCreateTokenFromDto(client.getId(), clientTokenRequestDto, createdBy);
    }

    @Override
    public void deleteToken(ClientToken clientToken) {
        clientToken.setClient(null);
        clientToken.setDeletedOn(Instant.now());

        clientTokenRepository.delete(clientToken);
    }

    @Override
    public void deleteToken(UUID id) {
        ClientToken clientToken = clientTokenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client token does not exist with id " + id));
        deleteToken(clientToken);
    }

    @Override
    public void deleteToken(UUID id, User deleteBy) {
        ClientToken clientToken = clientTokenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client token does not exist with id " + id));
        clientToken.setDeletedBy(deleteBy);

        deleteToken(clientToken);
    }

    @Override
    public boolean verifyToken(Client client, String token) {
        String tokenHash = passwordEncoder.encode(token);

        for (ClientToken clientToken : clientTokenRepository.findByClientId(client.getId())) {
            boolean tokenMatch = tokenHash.equals(clientToken.getTokenHash());

            if (tokenMatch && !isExpired(clientToken)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isExpired(ClientToken clientToken) {
        return clientToken.getExpiration().before(new Date(Instant.now().toEpochMilli()));
    }

    @Override
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date(Instant.now().toEpochMilli()));
    }
}
