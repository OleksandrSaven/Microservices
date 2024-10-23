package com.userservice.controller;

import com.userservice.dto.CreateUserRequestDto;
import com.userservice.dto.UserDto;
import com.userservice.dto.UserLoginRequestDto;
import com.userservice.dto.UserLoginResponseDto;
import com.userservice.exception.RegistrationException;
import com.userservice.security.AuthenticationService;
import com.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller")
@CrossOrigin
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody CreateUserRequestDto requestDto)
            throws RegistrationException {
        return userService.registration(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto getToken(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.generateToken(requestDto);
    }

    @Hidden
    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        return authenticationService.validateToken(token);
    }
}
