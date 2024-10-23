package com.userservice.controller;

import com.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User controller")
@Hidden
public class UserController {
    private final UserService userService;

    @GetMapping
    public Long getUserId(@RequestParam String userEmail) {
        return userService.getUserId(userEmail);
    }
}
