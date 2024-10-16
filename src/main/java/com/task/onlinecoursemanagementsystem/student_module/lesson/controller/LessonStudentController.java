package com.task.onlinecoursemanagementsystem.student_module.lesson.controller;

import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.service.LessonService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto.LessonGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("student/lessons")
@RequiredArgsConstructor
public class LessonStudentController {
    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping("courses")
    @PreAuthorize("hasRole('STUDENT')")
    public List<IdNameDto> getCourses(@RequestParam(required = false) String search) {
        return courseService.getCourses(search);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<LessonGetDto> getLessons(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(lessonService.getLessons(pageAndSortCriteria, courseId, search));
    }
}
