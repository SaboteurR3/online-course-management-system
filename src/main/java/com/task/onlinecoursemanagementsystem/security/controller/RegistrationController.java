package com.task.onlinecoursemanagementsystem.security.controller;


import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.service.AuthenticationService;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterInstructorRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterStudentRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthenticationService service;

    @PostMapping("student")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @NotNull @Valid RegisterStudentRequestDto request
    ) {
        return ResponseEntity.ok(service.registerStudent(request));
    }

    @PostMapping("instructor")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @NotNull @Valid RegisterInstructorRequestDto request
    ) {
        return ResponseEntity.ok(service.registerInstructor(request));
    }
}
