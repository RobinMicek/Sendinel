package cz.promtply.backend.service;

import cz.promtply.backend.dto.client.token.ClientTokenRequestDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.ClientToken;
import cz.promtply.backend.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientTokenService {
    Optional<ClientToken> getByClientId(UUID clientId);
    ClientToken createClientToken(ClientToken clientToken);
    String generateAndCreateTokenFromDto(UUID clientId, ClientTokenRequestDto clientTokenRequestDto, User user);
    String generateAndCreateTokenFromDto(Client client, ClientTokenRequestDto clientTokenRequestDto, User user);
    List<ClientToken> getAllClientTokens(Client client);
    List<ClientToken> getAllClientTokens(UUID clientId);
    void deleteToken(ClientToken clientToken);
    void deleteToken(UUID id);
    void deleteToken(UUID id, User deleteBy);
    boolean verifyToken(Client client, String token);
    boolean isExpired(ClientToken clientToken);
    boolean isExpired(Date expiration);
}
