package cz.promtply.backend.controller.internal;

import cz.promtply.backend.config.Constants;
import cz.promtply.backend.controller.InternalControllerBase;
import cz.promtply.backend.dto.PageResponseDto;
import cz.promtply.backend.dto.user.UserCreateRequestDto;
import cz.promtply.backend.dto.user.UserResponseDto;
import cz.promtply.backend.dto.user.UserUpdateRequestDto;
import cz.promtply.backend.entity.User;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.util.MapperUtil;
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

import java.util.UUID;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController extends InternalControllerBase {

    private final UserService userService;

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

}
