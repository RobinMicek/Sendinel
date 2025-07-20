package cz.promtply.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import cz.promtply.backend.dto.sender.SenderRequestDto;
import cz.promtply.backend.entity.Sender;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.models.SenderConfigurationField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface SenderService {
    Sender createSender(Sender sender);
    Sender createSenderFromDto(SenderRequestDto senderRequestDto, User createdBy);
    Optional<Sender> getSenderById(UUID id);
    Optional<Sender> getSenderByIdObfuscated(UUID id);
    List<Sender> getAllSenders();
    List<Sender> getAllSendersObfuscated();
    Page<Sender> getSenders(Pageable pageable);
    Page<Sender> getSendersObfuscated(Pageable pageable);
    Sender updateSender(UUID id, Sender sender);
    Sender updateSenderFromDto(UUID id, SenderRequestDto senderRequestDto, User updatedBy);
    void deleteSender(Sender sender);
    void deleteSender(UUID id);
    void deleteSender(UUID id, User deletedBy);


    JsonNode obfuscateSensitiveFields(JsonNode config, Map<String, SenderConfigurationField> schema);
}
