package cz.promtply.api.service;

import cz.promtply.api.dto.client.token.ClientTokenRequestDto;
import cz.promtply.api.entity.Client;
import cz.promtply.api.entity.ClientToken;
import cz.promtply.api.entity.User;
import cz.promtply.api.exceptions.ResourceNotFoundException;
import cz.promtply.api.repository.ClientTokenRepository;
import cz.promtply.api.util.TokenUtil;
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

        ClientToken savedClientToken = createClientToken(clientToken);

        return TokenUtil.combineToken(savedClientToken.getId(), token);
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
    public boolean isExpired(ClientToken clientToken) {
        return clientToken.getExpiration().before(new Date(Instant.now().toEpochMilli()));
    }

    @Override
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date(Instant.now().toEpochMilli()));
    }

    @Override
    public Optional<Client> getClientByToken(String combinedToken) {
        UUID tokenId = TokenUtil.extractTokenId(combinedToken);
        String token = TokenUtil.extractSecret(combinedToken);

        ClientToken clientToken = clientTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Client token does not exist"));

        if (!passwordEncoder.matches(token, clientToken.getTokenHash())) {
            throw new RuntimeException("Mismatch between token fingerprint and hash");
        }

        if (isExpired(clientToken)) {
            throw new ResourceNotFoundException("Token is expired");
        }

        clientToken.setLastUsedOn(Instant.now());
        clientTokenRepository.save(clientToken);

        return Optional.of(clientToken.getClient());
    }
}
