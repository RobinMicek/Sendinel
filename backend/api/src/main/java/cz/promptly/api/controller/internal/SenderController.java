package cz.promptly.api.controller.internal;

import cz.promptly.shared.config.Constants;
import cz.promptly.api.controller.InternalControllerBase;
import cz.promptly.api.dto.PageResponseDto;
import cz.promptly.api.dto.sender.SenderBasicsResponseDto;
import cz.promptly.api.dto.sender.SenderRequestDto;
import cz.promptly.api.dto.sender.SenderResponseDto;
import cz.promptly.api.entity.Sender;
import cz.promptly.api.service.SenderService;
import cz.promptly.api.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/sender")
@RequiredArgsConstructor
public class SenderController extends InternalControllerBase {

    private final SenderService senderService;

    @GetMapping
    @PreAuthorize("hasAuthority('SENDERS_READ')")
    public ResponseEntity<PageResponseDto<SenderResponseDto>> getSenders(Pageable pageable) {
        Page<Sender> senderPage = senderService.getSendersObfuscated(pageable);

        // Map Page<Sender> to Page<SenderResponseDto>
        Page<SenderResponseDto> dtoPage = senderPage.map(sender -> MapperUtil.toDto(sender, SenderResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    // For dropdowns
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('SENDERS_READ')")
    public ResponseEntity<List<SenderBasicsResponseDto>> getSendersList() {
        List<Sender> senders = senderService.getAllSendersObfuscated();

        // Map List<Sender> to List<SenderBasicsResponseDto>
        List<SenderBasicsResponseDto> response = senders.stream()
                .map(sender -> MapperUtil.toDto(sender, SenderBasicsResponseDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SENDERS_READ')")
    public ResponseEntity<SenderResponseDto> getSender(@PathVariable UUID id) {
        Sender sender = senderService.getSenderByIdObfuscated(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SENDERS_CREATE')")
    public ResponseEntity<SenderResponseDto> createSender(@Valid @RequestBody SenderRequestDto senderRequestDto) {
        Sender sender = senderService.createSenderFromDto(senderRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SENDERS_UPDATE')")
    public ResponseEntity<SenderResponseDto> updateSender(@PathVariable UUID id, @Valid @RequestBody SenderRequestDto senderRequestDto) {
        Sender sender = senderService.updateSenderFromDto(id, senderRequestDto, getLoggedInUser());

        return ResponseEntity.ok().body(MapperUtil.toDto(sender, SenderResponseDto.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SENDERS_DELETE')")
    public ResponseEntity<Void> deleteSender(@PathVariable UUID id) {
        senderService.deleteSender(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
