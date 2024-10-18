package com.task.onlinecoursemanagementsystem.security.controller;

import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterInstructorRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterStudentRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Registration", description = "Operations for user registration.")
public interface RegistrationControllerApi {

    @Operation(
            summary = "Register a Student",
            description = "Register a new student with the provided details."
    )
    @ApiResponse(responseCode = "200", description = "Successfully registered the student.")
    ResponseEntity<AuthenticationResponse> register(@Valid RegisterStudentRequestDto request);

    @Operation(
            summary = "Register an Instructor",
            description = "Register a new instructor with the provided details."
    )
    @ApiResponse(responseCode = "200", description = "Successfully registered the instructor.")
    ResponseEntity<AuthenticationResponse> register(@Valid RegisterInstructorRequestDto request);
}

