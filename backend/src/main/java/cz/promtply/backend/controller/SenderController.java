package cz.promtply.backend.controller;

import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.sender.SenderRequestDto;
import cz.promtply.backend.dto.sender.SenderResponseDto;
import cz.promtply.backend.entity.Sender;
import cz.promtply.backend.service.SenderService;
import cz.promtply.backend.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/sender")
@RequiredArgsConstructor
public class SenderController extends BaseUserLoggedInController {

    private final SenderService senderService;

    @GetMapping
    public ResponseEntity<PageResponseDto<SenderResponseDto>> getSenders(Pageable pageable) {
        Page<Sender> senderPage = senderService.getSenders(pageable);

        // Map Page<Sender> to Page<SenderResponseDto>
        Page<SenderResponseDto> dtoPage = senderPage.map(sender -> MapperUtil.toDto(sender, SenderResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SenderResponseDto> getSender(@PathVariable UUID id) {
        Sender sender = senderService.getSenderById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<SenderResponseDto> createSender(@Valid @RequestBody SenderRequestDto senderRequestDto) {
        Sender sender = senderService.createSenderFromDto(senderRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SenderResponseDto> updateSender(@PathVariable UUID id, @Valid @RequestBody SenderRequestDto senderRequestDto) {
        Sender sender = senderService.updateSenderFromDto(id, senderRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSender(@PathVariable UUID id) {
        senderService.deleteSender(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
