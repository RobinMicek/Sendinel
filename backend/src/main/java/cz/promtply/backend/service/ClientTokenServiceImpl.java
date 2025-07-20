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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
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
        String tokenFingerprint = generateTokenFingerprint(token);

        ClientToken clientToken = new ClientToken();
        clientToken.setName(clientTokenRequestDto.getName());
        clientToken.setDescription(clientTokenRequestDto.getDescription());
        clientToken.setExpiration(clientTokenRequestDto.getExpiration());
        clientToken.setTokenHash(tokenHash);
        clientToken.setTokenFingerprint(tokenFingerprint);
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

    @Override
    public Optional<Client> getClientByToken(String token) {
        String tokenFingerprint = generateTokenFingerprint(token);

        ClientToken clientToken = clientTokenRepository.findByTokenFingerprint(tokenFingerprint)
                .orElseThrow(() -> new ResourceNotFoundException("Client does not exist"));

        if (!passwordEncoder.matches(token, clientToken.getTokenHash())) {
            throw new RuntimeException("Mismatch between token fingerprint and hash");
        }

        if (isExpired(clientToken)) {
            throw new ResourceNotFoundException("Token is expired");
        }

        return Optional.of(clientToken.getClient());
    }


    private String generateTokenFingerprint(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
