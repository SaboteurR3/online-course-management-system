package com.task.onlinecoursemanagementsystem.common.review.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.review.repository.entity.Review;
import com.task.onlinecoursemanagementsystem.common.review.repository.ReviewRepository;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.Enrollment;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.EnrollmentStatus;
import com.task.onlinecoursemanagementsystem.student.enrollment.service.EnrollmentService;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.CourseReviewCreateDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.ReviewGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
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

        Enrollment enrollment = enrollmentService.findEnrollementByCourseAndUser(course, user);
        if(!EnrollmentStatus.COMPLETED.equals(enrollment.getStatus())) {
            throw new BusinessException("course is not completed");
        }

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
