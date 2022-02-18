package me.metropanties.fileserver.controller;

import lombok.RequiredArgsConstructor;
import me.metropanties.fileserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getall")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

}
