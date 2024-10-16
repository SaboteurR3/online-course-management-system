package com.task.onlinecoursemanagementsystem.student_module.review.controller.dto;

import java.time.LocalDateTime;

public interface ReviewGetDto {
    Long getId();

    Integer getRating();

    String getComment();

    LocalDateTime getCreateTs();

    String getCourseTitle();
}
