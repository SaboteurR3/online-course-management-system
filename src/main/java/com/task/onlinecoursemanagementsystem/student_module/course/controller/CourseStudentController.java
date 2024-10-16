package com.task.onlinecoursemanagementsystem.student_module.course.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student/courses")
@RequiredArgsConstructor
public class CourseStudentController {
    private final CourseService service;

    @GetMapping("categories")
    @PreAuthorize("hasRole('STUDENT')")
    public CourseCategory[] getCourseCategories() {
        return CourseCategory.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) CourseCategory category,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getCourses(pageAndSortCriteria, category, search));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public CourseDetailsGetDto getCourseById(@PathVariable Long id) {
        return service.getCourseDetailsById(id);
    }

    // other methods
}
