package cz.promtply.backend.service;

import cz.promtply.backend.dto.sender.SenderRequestDto;
import cz.promtply.backend.entity.Sender;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.exceptions.ResourceNotFoundException;
import cz.promtply.backend.repository.SenderRepository;
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
public class SenderServiceImpl implements SenderService {

    private final SenderRepository senderRepository;

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

        return createSender(sender);
    }

    @Override
    public Optional<Sender> getSenderById(UUID id) {
        return senderRepository.findById(id);
    }

    @Override
    public List<Sender> getAllSenders() {
        return senderRepository.findAll();
    }

    @Override
    public Page<Sender> getSenders(Pageable pageable) {
        return senderRepository.findAll(pageable);
    }

    @Override
    public Sender updateSender(UUID id, Sender sender) {
        Sender existingSender = senderRepository.findById(id).orElseThrow(
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
        Sender sender = new Sender();
        sender.setName(senderRequestDto.getName());
        sender.setType(senderRequestDto.getType());
        sender.setDescription(senderRequestDto.getDescription());
        sender.setConfiguration(senderRequestDto.getConfiguration());

        sender.setUpdatedBy(updatedBy);

        return updateSender(id, sender);
    }

    @Override
    public void deleteSender(Sender sender) {
        senderRepository.delete(sender);
    }

    @Override
    public void deleteSender(UUID id) {
        Sender sender = senderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sender not found with id " + id));
        deleteSender(sender);
    }

    @Override
    public void deleteSender(UUID id, User deletedBy) {
        Sender sender = senderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sender not found with id " + id));
        sender.setUpdatedBy(deletedBy);

        deleteSender(sender);
    }
}
