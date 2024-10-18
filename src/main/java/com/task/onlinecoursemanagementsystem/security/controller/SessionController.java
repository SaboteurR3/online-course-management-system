package com.task.onlinecoursemanagementsystem.security.controller;


import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping({
        "student/session",
        "instructor/session"
})
@RequiredArgsConstructor
public class SessionController implements SessionControllerApi {
    private final AuthenticationService service;

    @Override
    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequestDto request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Override
    @PostMapping("refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}
