package com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LessonCreateDto(
        @NotNull
        Long courseId,
        @NotEmpty
        String title,
        @NotEmpty
        String content,
        @NotNull
        Integer durationInMinutes,
        @NotNull
        @FutureOrPresent
        LocalDateTime startTime
) {
}
