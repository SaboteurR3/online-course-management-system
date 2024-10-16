package com.task.onlinecoursemanagementsystem.student_module.enrollment.controller.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ProgressUpdateDto(
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "100.0")
        Double newProgress
) {
}
