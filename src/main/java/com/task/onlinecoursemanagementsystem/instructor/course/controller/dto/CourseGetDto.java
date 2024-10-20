package com.task.onlinecoursemanagementsystem.instructor.course.controller.dto;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;

public interface CourseGetDto {
    Long getId();

    String getTitle();

    String getDescription();

    CourseCategory getCategory();

    Integer getMaxCapacity();

    Integer getCurrentCapacity();

    UserGetDto getInstructor();
}
