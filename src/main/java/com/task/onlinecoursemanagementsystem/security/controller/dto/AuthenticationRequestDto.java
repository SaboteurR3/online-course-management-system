package com.task.onlinecoursemanagementsystem.security.controller.dto;

import com.task.onlinecoursemanagementsystem.properties.AppConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AuthenticationRequestDto(
        @NotEmpty
        @Pattern(regexp = AppConstants.EMAIL_PATTERN)
        String email,
        @NotEmpty
        String password
) {
}
