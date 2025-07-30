package cz.promptly.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.promptly.shared.config.Constants;
import cz.promptly.api.dto.sender.SenderRequestDto;
import cz.promptly.api.entity.Sender;
import cz.promptly.api.entity.User;
import cz.promptly.api.exceptions.ResourceNotFoundException;
import cz.promptly.shared.models.SenderConfigurationField;
import cz.promptly.api.repository.SenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final SenderRepository senderRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Sender createSender(Sender sender) {
        sender.setCreatedOn(Instant.now());
        sender.setUpdatedOn(Instant.now());

        return senderRepository.save(sender);
    }

    @Override
    public Sender createSenderFromDto(SenderRequestDto senderRequestDto, User createdBy) {
        Sender sender = new Sender();
        sender.setName(senderRequestDto.getName());
        sender.setType(senderRequestDto.getType());
        sender.setDescription(senderRequestDto.getDescription());
        sender.setConfiguration(senderRequestDto.getConfiguration());

        sender.setCreatedBy(createdBy);
        sender.setUpdatedBy(createdBy);

        return obfuscateSenderConfig(createSender(sender));
    }

    @Override
    public Optional<Sender> getSenderById(UUID id) {
        return senderRepository.findByIdAndDeletedOnIsNull(id);
    }

    @Override
    public Optional<Sender> getSenderByIdObfuscated(UUID id) {
        return senderRepository.findByIdAndDeletedOnIsNull(id).map(this::obfuscateSenderConfig);
    }


    @Override
    public List<Sender> getAllSenders() {
        return senderRepository.findAllByDeletedOnIsNull();
    }

    @Override
    public List<Sender> getAllSendersObfuscated() {
        return senderRepository.findAllByDeletedOnIsNull().stream()
                .map(this::obfuscateSenderConfig)
                .collect(Collectors.toList());
    }


    @Override
    public Page<Sender> getSenders(Pageable pageable) {
        return senderRepository.findAllByDeletedOnIsNull(pageable);
    }

    @Override
    public Page<Sender> getSendersObfuscated(Pageable pageable) {
        return senderRepository.findAllByDeletedOnIsNull(pageable)
                .map(sender -> {
                    Map<String, SenderConfigurationField> schema = sender.getType().getConfigurationSchema();
                    JsonNode obfuscatedConfig = obfuscateSensitiveFields(sender.getConfiguration(), schema);
                    sender.setConfiguration(obfuscatedConfig);
                    return sender;
                });
    }


    @Override
    public Sender updateSender(UUID id, Sender sender) {
        Sender existingSender = senderRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(
                () -> new ResourceNotFoundException("Sender not found with id " + id)
        );

        existingSender.setName(sender.getName());
        existingSender.setType(sender.getType());
        existingSender.setDescription(sender.getDescription());
        existingSender.setConfiguration(sender.getConfiguration());

        existingSender.setUpdatedBy(existingSender.getUpdatedBy());
        existingSender.setUpdatedOn(Instant.now());

        return senderRepository.save(existingSender);
    }

    @Override
    public Sender updateSenderFromDto(UUID id, SenderRequestDto senderRequestDto, User updatedBy) {
        Sender existingSender = senderRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(
                () -> new ResourceNotFoundException("Sender not found with id " + id)
        );

        Sender sender = new Sender();
        sender.setName(senderRequestDto.getName());
        sender.setType(senderRequestDto.getType());
        sender.setDescription(senderRequestDto.getDescription());

        // Update sensitive values only if they are not null and don't match the obfuscation mask
        JsonNode oldConfig = existingSender.getConfiguration();
        JsonNode newConfig = senderRequestDto.getConfiguration();
        ObjectNode mergedConfig = oldConfig.deepCopy();

        Map<String, SenderConfigurationField> configSchema = senderRequestDto.getType().getConfigurationSchema();

        for (Map.Entry<String, SenderConfigurationField> entry : configSchema.entrySet()) {
            String field = entry.getKey();
            SenderConfigurationField fieldMeta = entry.getValue();

            JsonNode newValue = newConfig.get(field);

            if (fieldMeta.isSensitive()) {
                if (newValue != null && !newValue.isNull()) {
                    if (newValue.isTextual() && Constants.OBFUSCATION_MASK.equals(newValue.asText())) {
                        // Mask sent back, preserve old value
                        if (oldConfig.has(field)) {
                            mergedConfig.set(field, oldConfig.get(field));
                        } else {
                            mergedConfig.putNull(field); // fallback
                        }
                    } else {
                        // New non-mask value, update
                        mergedConfig.set(field, newValue);
                    }
                } else {
                    // Null value, preserve old value
                    if (oldConfig.has(field)) {
                        mergedConfig.set(field, oldConfig.get(field));
                    } else {
                        mergedConfig.putNull(field);
                    }
                }
            } else {
                // Always update non-obfuscated fields even if null
                mergedConfig.set(field, newValue);
            }
        }
        sender.setConfiguration(mergedConfig);

        sender.setUpdatedBy(updatedBy);

        return obfuscateSenderConfig(updateSender(id, sender));
    }

    @Override
    public void deleteSender(Sender sender) {
        senderRepository.delete(sender);
    }

    @Override
    public void deleteSender(UUID id) {
        Sender sender = senderRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Sender not found with id " + id));
        deleteSender(sender);
    }

    @Override
    public void deleteSender(UUID id, User deletedBy) {
        Sender sender = senderRepository.findByIdAndDeletedOnIsNull(id).orElseThrow(() -> new ResourceNotFoundException("Sender not found with id " + id));
        sender.setUpdatedBy(deletedBy);

        deleteSender(sender);
    }

    public JsonNode obfuscateSensitiveFields(JsonNode config, Map<String, SenderConfigurationField> schema) {
        ObjectNode obfuscated = objectMapper.createObjectNode();

        for (Map.Entry<String, SenderConfigurationField> entry : schema.entrySet()) {
            String field = entry.getKey();
            SenderConfigurationField meta = entry.getValue();

            if (config.has(field)) {
                if (meta.isSensitive()) {
                    obfuscated.put(field, Constants.OBFUSCATION_MASK);
                } else {
                    obfuscated.set(field, config.get(field));
                }
            }
        }

        return obfuscated;
    }

    private Sender obfuscateSenderConfig(Sender sender) {
        // Clone the sender to avoid modifying the original entity
        Sender obfuscatedSender = new Sender();
        obfuscatedSender.setId(sender.getId());
        obfuscatedSender.setName(sender.getName());
        obfuscatedSender.setType(sender.getType());
        obfuscatedSender.setDescription(sender.getDescription());
        obfuscatedSender.setCreatedBy(sender.getCreatedBy());
        obfuscatedSender.setUpdatedBy(sender.getUpdatedBy());
        obfuscatedSender.setCreatedOn(sender.getCreatedOn());
        obfuscatedSender.setUpdatedOn(sender.getUpdatedOn());
        obfuscatedSender.setDeletedOn(sender.getDeletedOn());

        // Obfuscate only the configuration field
        Map<String, SenderConfigurationField> schema = sender.getType().getConfigurationSchema();
        JsonNode obfuscatedConfig = obfuscateSensitiveFields(sender.getConfiguration(), schema);
        obfuscatedSender.setConfiguration(obfuscatedConfig);

        return obfuscatedSender;
    }

}
