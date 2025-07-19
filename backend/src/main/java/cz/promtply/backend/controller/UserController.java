package cz.promtply.backend.controller;

import com.google.zxing.WriterException;
import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.auth.TotpRequestDto;
import cz.promtply.backend.dto.user.UserCreateRequestDto;
import cz.promtply.backend.dto.user.UserResponseDto;
import cz.promtply.backend.dto.user.UserUpdateRequestDto;
import cz.promtply.backend.dto.user.totp.UserTotpCreateResponseDto;
import cz.promtply.backend.dto.user.totp.UserTotpStatusResponseDto;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.entity.UserTotp;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.service.UserTotpService;
import cz.promtply.backend.util.MapperUtil;
import cz.promtply.backend.util.TotpUtil;
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

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseUserLoggedInController {

    private final UserService userService;
    private final UserTotpService userTotpService;
    private final TotpUtil totpUtil;

    @GetMapping
    public ResponseEntity<PageResponseDto<UserResponseDto>> getUsers(Pageable pageable) {
        Page<User> userPage = userService.getUsers(pageable);

        // Map Page<User> to Page<UserResponseDto>
        Page<UserResponseDto> dtoPage = userPage.map(user -> MapperUtil.toDto(user, UserResponseDto.class));

        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );

        return ResponseEntity.ok(MapperUtil.toDto(user, UserResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.createUserFromDto(userCreateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(user, UserResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        User user = userService.updateUserFromDto(id, userUpdateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(user, UserResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/totp")
    public ResponseEntity<UserTotpStatusResponseDto> getTotpStatus() {
        UserTotpStatusResponseDto response = new UserTotpStatusResponseDto(
                userService.hasTotp(getLoggedInUser().getId()),
                userService.hasTotpActivated(getLoggedInUser().getId())
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/totp")
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
    public ResponseEntity<Void> deleteTotp() {
        userTotpService.deleteTotp(getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/totp/activate")
    public ResponseEntity<Void> activateTotp(@Valid @RequestBody TotpRequestDto totpRequestDto) {
        userTotpService.activateTotp(totpRequestDto.getCode(), getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
