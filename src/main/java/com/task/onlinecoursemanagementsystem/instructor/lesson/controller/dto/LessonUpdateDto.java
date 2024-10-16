package com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record LessonUpdateDto(
        @NotEmpty
        String title,
        @NotEmpty
        String content,
        @NotNull
        Integer durationInMinutes,
        @NotNull
        @FutureOrPresent
        LocalDateTime startTime,
        List<@NotNull Long> attachmentIds
) {
}
