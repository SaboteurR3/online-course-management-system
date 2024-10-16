package com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;

public interface CourseGetDto {
    Long getId();

    String getTitle();

    String getDescription();

    CourseCategory getCategory();

    UserGetDto getInstructor();
}
