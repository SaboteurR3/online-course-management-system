package com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CourseLessonsGetDto(
        String title,
        String content,
        Integer duration,
        LocalDateTime startTime
) {
}
