package cz.promptly.api.controller.external;

import cz.promptly.api.controller.ExternalControllerBase;
import cz.promptly.api.dto.email.EmailRequestDto;
import cz.promptly.api.dto.email.EmailResponseDto;
import cz.promptly.api.entity.Email;
import cz.promptly.api.service.EmailService;
import cz.promptly.api.util.MapperUtil;
import cz.promptly.shared.config.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.EXTERNAL_API_ROUTE_PREFIX + "/client-api")
@RequiredArgsConstructor
public class ClientApiController extends ExternalControllerBase {

    private final EmailService emailService;

    @PostMapping("/email")
    @PreAuthorize("hasAuthority('EMAILS_CREATE')")
    public ResponseEntity<EmailResponseDto> createEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        Email email = emailService.createEmailFromDtoAndSendJob(emailRequestDto, getLoggedInClient());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(email, EmailResponseDto.class));
    }
}
