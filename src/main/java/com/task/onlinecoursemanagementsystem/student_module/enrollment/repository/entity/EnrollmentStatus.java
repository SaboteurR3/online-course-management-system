package com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnrollmentStatus {
    ACTIVE("აქტიური"),
    COMPLETED("დასრულებული"),
    IN_PROGRESS("მიმდინარე");

    private final String nameKa;
}
