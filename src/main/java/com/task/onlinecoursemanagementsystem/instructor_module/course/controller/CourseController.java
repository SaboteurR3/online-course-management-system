package com.task.onlinecoursemanagementsystem.instructor_module.course.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("instructor/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @GetMapping("categories")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseCategory[] getCourseCategories() {
        return CourseCategory.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) CourseCategory category,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(service.getCourses(pageAndSortCriteria, category, search));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseDetailsGetDto getCourseById(@PathVariable Long id) {
        return service.getCourseDetailsById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@Valid @RequestBody CourseCreateDto dto) {
        service.createCourse(dto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateDto dto
    ) {
        service.updateCourse(id, dto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        service.deleteCourse(id);
    }
}
