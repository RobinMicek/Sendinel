package cz.sendinel.api.controller.internal;

import cz.sendinel.shared.config.Constants;
import cz.sendinel.api.controller.InternalControllerBase;
import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.email.EmailResponseDto;
import cz.sendinel.api.entity.Email;
import cz.sendinel.api.service.ClientService;
import cz.sendinel.api.service.EmailService;
import cz.sendinel.api.service.EmailStatusService;
import cz.sendinel.api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/email")
@RequiredArgsConstructor
public class EmailController extends InternalControllerBase {

    private final EmailService emailService;
    private final EmailStatusService emailStatusService;
    private final ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMAILS_READ')")
    public ResponseEntity<PageResponseDto<EmailResponseDto>> getEmails(Pageable pageable) {
        Page<Email> emailPage = emailService.getEmails(pageable);
        emailPage.forEach(email -> {email.setEmailStatuses(emailStatusService.getByEmail(email));});

        // Map Page<Email> to Page<EmailResponseDto>
        Page<EmailResponseDto> dtoPage = emailPage.map(email -> MapperUtil.toDto(email, EmailResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMAILS_READ')")
    public ResponseEntity<EmailResponseDto> getEmail(@PathVariable UUID id) {
        Email email = emailService.getEmailById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist")
        );

        email.setEmailStatuses(emailStatusService.getByEmail(email));

        return ResponseEntity.ok(MapperUtil.toDto(email, EmailResponseDto.class));
    }

    @GetMapping("/{id}/render")
    @PreAuthorize("hasAuthority('EMAILS_READ')")
    public ResponseEntity<InputStreamResource> renderEmailToPDF(@PathVariable UUID id) throws IOException {
        Email email = emailService.getEmailById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email does not exist with id " + id)
        );

        File pdfFile = emailService.renderEmailToPDF(email);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        String filename = "email_render_" + id +  ".pdf";
        String filenameEncoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filenameEncoded)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfFile.length())
                .body(resource);
    }

}
