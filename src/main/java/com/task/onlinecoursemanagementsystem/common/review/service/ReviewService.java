package com.task.onlinecoursemanagementsystem.common.review.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.review.repository.entity.Review;
import com.task.onlinecoursemanagementsystem.common.review.repository.ReviewRepository;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.CourseReviewCreateDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.ReviewGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final CourseService courseService;
    private final ReviewRepository repository;

    public Page<ReviewGetDto> getReviews(
            PageAndSortCriteria pageAndSortCriteria,
            Long courseId,
            Long rating,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        User user = userService.curentUser();
        return repository.getReviews(pageable, courseId, rating, user.getId(), search);
    }

    public Page<ReviewGetDto> getCourseReviews(
            PageAndSortCriteria pageAndSortCriteria,
            Long courseId,
            Long rating,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        User user = userService.curentUser();
        return repository.getCourseReviews(pageable, courseId, rating, user.getId(), search);
    }

    public void createReview(CourseReviewCreateDto data) {
        Course course = courseService.lookupCourse(data.courseId());
        User user = userService.curentUser();
        Review review = Review.builder()
                .course(course)
                .student(user)
                .rating(data.rating())
                .comment(data.comment())
                .createTs(LocalDateTime.now())
                .build();
        repository.saveAndFlush(review);
    }

}
