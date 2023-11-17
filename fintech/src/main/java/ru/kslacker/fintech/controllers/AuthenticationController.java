package ru.kslacker.fintech.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.fintech.dto.UserDto;
import ru.kslacker.fintech.models.user.UserModel;
import ru.kslacker.fintech.service.api.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "register", consumes = "application/json", produces = "application/json")
    public UserDto registerUser(@RequestBody UserModel userModel) {
        return userService.create(userModel.credentials());
    }
}
