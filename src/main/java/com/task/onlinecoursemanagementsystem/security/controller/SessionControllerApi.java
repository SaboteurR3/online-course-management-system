package com.task.onlinecoursemanagementsystem.security.controller;

import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Tag(name = "Session", description = "Operations for user authentication and session management.")
public interface SessionControllerApi {

    @Operation(
            summary = "Authenticate User",
            description = "Authenticate a user (student or instructor) with the provided credentials."
    )
    @ApiResponse(responseCode = "200", description = "Successfully authenticated the user.")
    ResponseEntity<AuthenticationResponse> authenticate(@Valid AuthenticationRequestDto request);

    @Operation(
            summary = "Refresh Authentication Token",
            description = "Refresh the user's authentication token."
    )
    @ApiResponse(responseCode = "200", description = "Successfully refreshed the authentication token.")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

