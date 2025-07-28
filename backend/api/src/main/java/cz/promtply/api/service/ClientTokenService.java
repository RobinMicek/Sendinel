package cz.promtply.api.service;

import cz.promtply.api.dto.client.token.ClientTokenRequestDto;
import cz.promtply.api.entity.Client;
import cz.promtply.api.entity.ClientToken;
import cz.promtply.api.entity.User;

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

    boolean isExpired(ClientToken clientToken);
    boolean isExpired(Date expiration);
    Optional<Client> getClientByToken(String token);
}
