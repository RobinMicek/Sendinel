package cz.promtply.backend.controller;

import cz.promtply.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HelloController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello, World!");
    }
}
