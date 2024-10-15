package com.task.onlinecoursemanagementsystem.security.user.repository.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_CREATE("student:create"),
    STUDENT_DELETE("student:delete"),
    ;

    private final String permission;
}
