package com.task.onlinecoursemanagementsystem.instructor.course.controller.dto;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import lombok.Builder;

import java.util.List;

@Builder
public record CourseDetailsGetDto(
        String title,
        String description,
        CourseCategory category,
        Integer maxCapacity,
        Integer currentCapacity,
        String instructorName,
        String instructorEmail,
        List<CourseLessonsGetDto> lessons
) {
}
