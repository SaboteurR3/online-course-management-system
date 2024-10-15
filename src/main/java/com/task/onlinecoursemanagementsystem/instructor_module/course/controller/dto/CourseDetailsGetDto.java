package com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record CourseDetailsGetDto(
        String title,
        String description,
        CourseCategory category,
        User instructorName,
        List<User> students,
        List<Lesson> lessons
) {
}
