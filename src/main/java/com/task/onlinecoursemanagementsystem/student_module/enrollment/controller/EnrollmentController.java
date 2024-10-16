package com.task.onlinecoursemanagementsystem.student_module.enrollment.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.controller.dto.EnrollmentsGetDto;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("course-categories")
    @PreAuthorize("hasRole('STUDENT')")
    public CourseCategory[] getCourseCategories() {
        return CourseCategory.values();
    }

    @GetMapping("courses")
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) CourseCategory category,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(courseService.getCourses(pageAndSortCriteria, category, search));
    }

    @GetMapping("courses/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public CourseDetailsGetDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseDetailsById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<EnrollmentsGetDto> getEnrollments(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(enrollmentService.getEnrollments(pageAndSortCriteria, courseId, search));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public void enrollStudent(@RequestParam Long courseId) {
        enrollmentService.enrollStudent(courseId);
    }

//    @PutMapping("{id}/progress")
//    @PreAuthorize("hasRole('STUDENT')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void updateProgress(
//            @PathVariable Long id,
//            @Valid @RequestBody ProgressUpdateDto progressUpdateDto) {
//        enrollmentService.updateProgress(id, progressUpdateDto);
//    }
}
