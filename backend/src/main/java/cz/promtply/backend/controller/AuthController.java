package cz.promtply.backend.controller;

import cz.promtply.backend.dto.auth.JwtResponseDto;
import cz.promtply.backend.dto.auth.LoginRequestDto;
import cz.promtply.backend.dto.auth.TotpRequestDto;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.service.UserTotpService;
import cz.promtply.backend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserTotpService userTotpService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        User user = userService.getUserByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        JwtResponseDto jwt = new JwtResponseDto(jwtUtil.generateToken(user.getId(), user.getRole().name(), false));
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/totp")
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
