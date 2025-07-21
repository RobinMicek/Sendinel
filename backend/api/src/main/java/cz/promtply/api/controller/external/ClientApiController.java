package cz.promtply.api.controller.external;

import cz.promtply.api.config.Constants;
import cz.promtply.api.controller.ExternalControllerBase;
import cz.promtply.api.dto.email.EmailRequestDto;
import cz.promtply.api.dto.email.EmailResponseDto;
import cz.promtply.api.entity.Email;
import cz.promtply.api.service.EmailService;
import cz.promtply.api.util.MapperUtil;
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
