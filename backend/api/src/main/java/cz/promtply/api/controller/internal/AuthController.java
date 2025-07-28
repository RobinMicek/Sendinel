package cz.promtply.api.controller.internal;

import com.google.zxing.WriterException;
import cz.promtply.shared.config.Constants;
import cz.promtply.api.controller.InternalControllerBase;
import cz.promtply.api.dto.auth.JwtResponseDto;
import cz.promtply.api.dto.auth.LoginRequestDto;
import cz.promtply.api.dto.auth.TotpRequestDto;
import cz.promtply.api.dto.user.totp.UserTotpCreateResponseDto;
import cz.promtply.api.dto.user.totp.UserTotpStatusResponseDto;
import cz.promtply.api.entity.User;
import cz.promtply.shared.enums.UserRolesEnum;
import cz.promtply.api.service.UserService;
import cz.promtply.api.service.UserTotpService;
import cz.promtply.api.util.JwtUtil;
import cz.promtply.api.util.TotpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController extends InternalControllerBase {

    private final UserService userService;
    private final UserTotpService userTotpService;
    private final JwtUtil jwtUtil;
    private final TotpUtil totpUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        User user = userService.getUserByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        JwtResponseDto jwt = new JwtResponseDto(jwtUtil.generateToken(user.getId(), UserRolesEnum.NON_TOTP.name(), false));
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/totp")
    @PreAuthorize("hasAuthority('AUTH_TOTP_READ')")
    public ResponseEntity<UserTotpStatusResponseDto> getTotpStatus() {
        UserTotpStatusResponseDto response = new UserTotpStatusResponseDto(
                userService.hasTotp(getLoggedInUser().getId()),
                userService.hasTotpActivated(getLoggedInUser().getId())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/totp")
    @PreAuthorize("hasAuthority('AUTH_TOTP_CREATE')")
    public ResponseEntity<UserTotpCreateResponseDto> createTotp() throws IOException, WriterException {
        String secret = userTotpService.generateAndCreateTotp(getLoggedInUser());
        String qrCode = totpUtil.generateQRCodeBase64(getLoggedInUser().getEmail(), secret);

        return ResponseEntity.ok(
                new UserTotpCreateResponseDto(
                        secret,
                        qrCode
                )
        );
    }

    @DeleteMapping("/totp")
    @PreAuthorize("hasAuthority('AUTH_TOTP_DELETE')")
    public ResponseEntity<Void> deleteTotp() {
        userTotpService.deleteTotp(getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/totp/activate")
    @PreAuthorize("hasAuthority('AUTH_TOTP_CREATE')")
    public ResponseEntity<Void> activateTotp(@Valid @RequestBody TotpRequestDto totpRequestDto) {
        userTotpService.activateTotp(totpRequestDto.getCode(), getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/totp/verify")
    public ResponseEntity<JwtResponseDto> verifyTotp(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid TotpRequestDto dto) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtUtil.getUserId(token);

        if (jwtUtil.isTotpVerified(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TOTP already verified");
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (user.getTotp() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TOTP is not configured");

        if (!user.getTotp().isActivated()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TOTP is not activated");

        boolean valid = userTotpService.verifyTotp(dto.getCode(), user);
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid TOTP code");
        }

        JwtResponseDto jwt = new JwtResponseDto(jwtUtil.generateToken(user.getId(), user.getRole().name(), true));
        return ResponseEntity.ok(jwt);
    }
}
