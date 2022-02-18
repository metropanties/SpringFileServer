package me.metropanties.fileserver.controller;

import lombok.RequiredArgsConstructor;
import me.metropanties.fileserver.model.User;
import me.metropanties.fileserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login() {
        return ResponseEntity.ok("login");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        if (user == null) {
            Map<Object, Object> body = Map.of(
                    "message", "Please provide a valid body!",
                    "code", HttpStatus.BAD_REQUEST.value()
            );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        String username = user.getUsername();
        if (userService.validateUsername(username)) {
            Map<Object, Object> body = Map.of(
                    "message", "Username already in use!",
                    "code", HttpStatus.CONFLICT.value()
            );
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        userService.saveUser(user);
        Map<Object, Object> body = Map.of(
                "message", "Successfully registered!",
                "code", HttpStatus.CREATED.value(),
                user.getClass().getSimpleName(), user
        );
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

}
