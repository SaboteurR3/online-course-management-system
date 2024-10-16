package com.task.onlinecoursemanagementsystem.student_module.review.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.common.review.service.ReviewService;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.student_module.review.controller.dto.CourseReviewCreateDto;
import com.task.onlinecoursemanagementsystem.student_module.review.controller.dto.ReviewGetDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final CourseService courseService;

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

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<ReviewGetDto> getReviews(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) @Min(value = 1) @Max(value = 10) Long rating,
            @RequestParam(required = false) String search) {
        return PageView.of(reviewService.getReviews(pageAndSortCriteria, courseId, rating, search));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReview(@Valid @RequestBody CourseReviewCreateDto data) {
        reviewService.createReview(data);
    }
}
