package cz.promtply.backend.controller;

import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.email.EmailRequestDto;
import cz.promtply.backend.dto.email.EmailResponseDto;
import cz.promtply.backend.entity.Client;
import cz.promtply.backend.entity.Email;
import cz.promtply.backend.service.ClientService;
import cz.promtply.backend.service.EmailService;
import cz.promtply.backend.service.EmailStatusService;
import cz.promtply.backend.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController extends BaseUserLoggedInController {

    private final EmailService emailService;
    private final EmailStatusService emailStatusService;
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<PageResponseDto<EmailResponseDto>> getEmails(Pageable pageable) {
        Page<Email> emailPage = emailService.getEmails(pageable);
        emailPage.forEach(email -> {email.setEmailStatuses(emailStatusService.getByEmail(email));});

        // Map Page<Email> to Page<EmailResponseDto>
        Page<EmailResponseDto> dtoPage = emailPage.map(email -> MapperUtil.toDto(email, EmailResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailResponseDto> getEmail(@PathVariable UUID id) {
        Email email = emailService.getEmailById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist")
        );

        email.setEmailStatuses(emailStatusService.getByEmail(email));

        return ResponseEntity.ok(MapperUtil.toDto(email, EmailResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<EmailResponseDto> createClient(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        // Replace with actual logic
        Client client = clientService.getAllClients().getFirst();

        Email email = emailService.createEmailFromDto(emailRequestDto, client);

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(email, EmailResponseDto.class));
    }

}
