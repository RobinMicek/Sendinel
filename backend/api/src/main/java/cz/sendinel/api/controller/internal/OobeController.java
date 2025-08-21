package cz.sendinel.api.controller.internal;

import cz.sendinel.api.dto.oobe.OobeStatusResponseDto;
import cz.sendinel.api.dto.user.UserCreateRequestDto;
import cz.sendinel.api.dto.user.UserResponseDto;
import cz.sendinel.api.entity.User;
import cz.sendinel.api.service.UserService;
import cz.sendinel.api.util.MapperUtil;
import cz.sendinel.shared.config.Constants;
import cz.sendinel.shared.enums.UserRolesEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERNAL_API_ROUTE_PREFIX + "/oobe")
@RequiredArgsConstructor
public class OobeController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<OobeStatusResponseDto> isOobe() {
        OobeStatusResponseDto response = new OobeStatusResponseDto();
        response.setOobeStatus(userService.isOobe());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createFirstUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        if (!userService.isOobe()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userCreateRequestDto.setRole(UserRolesEnum.ADMIN); // First user is always admin
        User user = userService.createUserFromDto(userCreateRequestDto, null); // Has no createdBy

        return ResponseEntity.status(HttpStatus.CREATED).body(MapperUtil.toDto(user, UserResponseDto.class));
    }

}
