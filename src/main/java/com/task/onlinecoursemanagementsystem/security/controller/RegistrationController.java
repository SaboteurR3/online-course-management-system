package com.task.onlinecoursemanagementsystem.security.controller;


import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterInstructorRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterStudentRequestDto;
import com.task.onlinecoursemanagementsystem.security.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registration")
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerApi {
    private final RegistrationService service;

    @Override
    @PostMapping("student")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterStudentRequestDto request
    ) {
        return ResponseEntity.ok(service.registerStudent(request));
    }

    @Override
    @PostMapping("instructor")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterInstructorRequestDto request
    ) {
        return ResponseEntity.ok(service.registerInstructor(request));
    }
}
