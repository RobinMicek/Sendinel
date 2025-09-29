package cz.sendinel.api.controller.internal;

import cz.sendinel.api.dto.user.UserChangePasswordRequestDto;
import cz.sendinel.api.util.RsqlUtil;
import cz.sendinel.shared.config.Constants;
import cz.sendinel.api.controller.InternalControllerBase;
import cz.sendinel.api.dto.PageResponseDto;
import cz.sendinel.api.dto.user.UserCreateRequestDto;
import cz.sendinel.api.dto.user.UserResponseDto;
import cz.sendinel.api.dto.user.UserUpdateRequestDto;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.service.UserService;
import cz.sendinel.api.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController extends InternalControllerBase {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('USERS_READ')")
    public ResponseEntity<PageResponseDto<UserResponseDto>> getUsers(Pageable pageable, @RequestParam(required = false) String search) {
        Specification<User> spec = RsqlUtil.toSpecification((search == null || search.isBlank())? "deletedOn==null" : String.format("(%s);deletedOn==null", search));
        Page<User> userPage = userService.getUsers(pageable, spec);

        Page<UserResponseDto> dtoPage = userPage.map(MapperUtil::userToDto);
        return ResponseEntity.ok(MapperUtil.fromPage(dtoPage));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_READ')")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );

        return ResponseEntity.ok(MapperUtil.userToDto(user));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USERS_READ')")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        return ResponseEntity.ok(MapperUtil.userToDto(getLoggedInUser()));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('USERS_CREATE')")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.createUserFromDto(userCreateRequestDto, getLoggedInUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.userToDto(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_UPDATE')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        User user = userService.updateUserFromDto(id, userUpdateRequestDto, getLoggedInUser());

        return ResponseEntity.ok().body(MapperUtil.userToDto(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_DELETE')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/change-password")
    @PreAuthorize("hasAuthority('CHANGE_OWN_PASSWORD')")
    public ResponseEntity<UserResponseDto> changeOwnPassword(@Valid @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) {
        userService.changePassword(getLoggedInUser(), userChangePasswordRequestDto.getPassword(), getLoggedInUser());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasAuthority('USERS_CHANGE_PASSWORD')")
    public ResponseEntity<UserResponseDto> changePassword(@PathVariable UUID id, @Valid @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) {
        userService.changePasswordFromDto(id, userChangePasswordRequestDto, getLoggedInUser());

        return ResponseEntity.noContent().build();
    }
}
