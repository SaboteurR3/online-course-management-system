package com.task.onlinecoursemanagementsystem.student.review.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CourseReviewCreateDto(
        @NotNull
        Long courseId,
        @NotNull
        @Min(value = 1)
        @Max(value = 10)
        Integer rating,
        String comment
) {
}
