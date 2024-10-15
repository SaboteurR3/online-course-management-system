package com.task.onlinecoursemanagementsystem.security.user.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    STUDENT("სტუდენტი"),
    INSTRUCTOR("ინსტრუქტორი");

    private final String nameKa;
}
