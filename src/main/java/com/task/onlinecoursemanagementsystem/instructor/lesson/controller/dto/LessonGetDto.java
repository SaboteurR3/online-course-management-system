package com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto;

import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;

import java.time.LocalDateTime;

public interface LessonGetDto {
    Long getLessonId();

    String getLessonTitle();

    String getLessonContent();

    String getDurationInMinutes();

    LocalDateTime getStartTime();

    String getCourseName();

    UserGetDto getInstructor();
}
