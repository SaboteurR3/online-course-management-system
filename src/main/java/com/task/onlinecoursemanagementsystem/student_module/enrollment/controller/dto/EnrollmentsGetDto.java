package com.task.onlinecoursemanagementsystem.student_module.enrollment.controller.dto;

import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.entity.EnrollmentStatus;

import java.time.LocalDateTime;

public interface EnrollmentsGetDto {
    Long getId();

    UserGetDto getStudent();

    String getCourseTitle();

    String getCourseDescription();

    Long getCourseId();

    LocalDateTime getEnrollmentDate();

    Double getProgress();

    EnrollmentStatus getStatus();
}
