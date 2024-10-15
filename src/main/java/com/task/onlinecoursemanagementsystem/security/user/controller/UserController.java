package com.task.onlinecoursemanagementsystem.security.user.controller;

import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PermitAll
    public UserDTO getUserProfile() {
        return userService.getProfile();
    }
}
