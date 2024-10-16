package com.task.onlinecoursemanagementsystem.student_module.enrollment.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.controller.dto.EnrollmentsGetDto;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.EnrollmentRepository;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.entity.Enrollment;
import com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.entity.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final UserService userService;
    private final CourseService courseService;
    private final EnrollmentRepository repository;

    public Enrollment lookupEnrollment(Long id) {
        return repository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<EnrollmentsGetDto> getEnrollments(
            PageAndSortCriteria pageAndSortCriteria,
            Long courseId,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        Course course = courseService.lookupCourse(courseId);
        return repository.getEnrollments(pageable, courseId, course.getId(), search);
    }

//    public Page<LessonGetDto> getAllEnrollmentStatus(Long courseId) {
//        return repository.getCurrentEnrollment(courseId, search);
//    }

    public void enrollStudent(Long courseId) {
        User currentStudent = userService.curentUser();
        Course course = courseService.lookupCourse(courseId);
        Enrollment enrollment = Enrollment.builder()
                .student(currentStudent)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .progress(0.0)
                .status(EnrollmentStatus.ACTIVE)
                .build();
        try {
            repository.saveAndFlush(enrollment);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_course_title_start_time", "course_title_start_time_must_be_unique"));
        }
    }

    public void updateProgress(Long id) {

    }
}
