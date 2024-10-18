package com.task.onlinecoursemanagementsystem.security.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Profile", description = "Operations related to user profile management.")
public interface UserControllerApi {

    @Operation(
            summary = "Get User Profile",
            description = "Retrieve the user's profile information. Accessible to all users."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the user profile.")
    UserDTO getUserProfile();
}

