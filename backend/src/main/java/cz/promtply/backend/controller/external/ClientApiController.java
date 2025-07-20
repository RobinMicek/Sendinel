package cz.promtply.backend.controller.external;

import cz.promtply.backend.config.Constants;
import cz.promtply.backend.controller.ExternalControllerBase;
import cz.promtply.backend.dto.email.EmailRequestDto;
import cz.promtply.backend.dto.email.EmailResponseDto;
import cz.promtply.backend.entity.Email;
import cz.promtply.backend.service.EmailService;
import cz.promtply.backend.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EmailResponseDto> createEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        Email email = emailService.createEmailFromDto(emailRequestDto, getLoggedInClient());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(email, EmailResponseDto.class));
    }
}
