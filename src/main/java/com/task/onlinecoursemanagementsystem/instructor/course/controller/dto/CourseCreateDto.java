package com.task.onlinecoursemanagementsystem.instructor.course.controller.dto;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CourseCreateDto(
        @NotEmpty
        String title,
        @NotEmpty
        String description,
        @NotNull
        CourseCategory category,
        @NotNull
        Integer maxCapacity
) {
}
