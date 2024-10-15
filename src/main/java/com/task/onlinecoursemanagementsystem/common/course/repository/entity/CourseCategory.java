package com.task.onlinecoursemanagementsystem.common.course.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum CourseCategory {
    SCIENCE("მეცნიერება"),
    TECHNOLOGY("ტექნოლოგია"),
    MATHEMATICS("მათემატიკა"),
    ARTS("ხელოვნება"),
    BUSINESS("ბიზნესი");

    private final String nameKa;

    public String getCode() {
        return this.name();
    }
}
