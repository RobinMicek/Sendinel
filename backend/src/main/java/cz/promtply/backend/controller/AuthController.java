package cz.promtply.backend.controller;

import cz.promtply.backend.dto.auth.JwtDto;
import cz.promtply.backend.dto.auth.LoginDto;
import cz.promtply.backend.dto.auth.TotpDto;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.util.JwtUtil;
import cz.promtply.backend.util.TotpUtil;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody @Valid LoginDto dto) {
        User user = userService.getUserByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        JwtDto jwt = new JwtDto(jwtUtil.generateToken(user.getEmail(), user.getRole().name(), false));
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/totp")
    public ResponseEntity<JwtDto> verifyTotp(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid TotpDto dto) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmail(token);

        if (jwtUtil.isTotpVerified(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TOTP already verified");
        }

        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        System.out.println(user.getTotp());
        if (user.getTotp() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TOTP is not configured");

        boolean valid = TotpUtil.verifyTotp(user.getTotp().getSecret(), dto.getCode());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid TOTP code");
        }

        JwtDto jwt = new JwtDto(jwtUtil.generateToken(email, user.getRole().name(), true));
        return ResponseEntity.ok(jwt);
    }
}
