package cz.promtply.backend.controller;

import cz.promtply.backend.dto.user.UserDto;
import cz.promtply.backend.service.UserService;
import cz.promtply.backend.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> hello() {
        return ResponseEntity.ok(
                userService.getAllUsers()
                        .stream()
                        .map(user -> MapperUtil.toDto(user, UserDto.class))
                        .collect(Collectors.toList())
        );
    }
}
