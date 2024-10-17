package com.task.onlinecoursemanagementsystem.student.enrollment.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import com.task.onlinecoursemanagementsystem.student.enrollment.controller.dto.EnrollmentsGetDto;
import com.task.onlinecoursemanagementsystem.student.enrollment.controller.dto.ProgressUpdateDto;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.EnrollmentRepository;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.Enrollment;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.EnrollmentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
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
        User user = userService.curentUser();
        return repository.getEnrollments(pageable, courseId, user.getId(), true, search);
    }

    public void enrollStudent(Long courseId) {
        User currentStudent = userService.curentUser();
        Course course = courseService.lookupCourse(courseId);
        course.getStudents().add(currentStudent);
        Enrollment enrollment = Enrollment.builder()
                .student(currentStudent)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .progress(BigDecimal.ZERO)
                .status(EnrollmentStatus.ACTIVE)
                .active(true)
                .build();
        try {
            repository.saveAndFlush(enrollment);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("unique_enrollment", "student_already_enrolled_in_current_course"));
        }
    }

    public void updateProgress(Long id, ProgressUpdateDto data) {
        Enrollment enrollment = lookupEnrollment(id);
        if(!enrollment.isActive()) {
            throw new BusinessException("course is not active");
        }

        User currentStudent = userService.curentUser();
        if (!currentStudent.equals(enrollment.getStudent())) {
            throw new SecurityViolationException();
        }

        if(EnrollmentStatus.COMPLETED.equals(enrollment.getStatus())) {
            throw new BusinessException("course is already completed");
        }
        enrollment.setProgress(data.newProgress());
        if(BigDecimal.valueOf(100).equals(enrollment.getProgress())) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
        }
    }

    public void unenrollFromCourse(Long id) {
        Enrollment enrollment = lookupEnrollment(id);
        enrollment.setActive(false);
    }

    public Enrollment findEnrollementByCourseAndUser(Course course, User user) {
        Optional<Enrollment> byCourseAndStudent = repository.findByCourseAndStudent(course, user);
        if(byCourseAndStudent.isEmpty()){
            throw new SecurityViolationException();
        }
        return byCourseAndStudent.get();
    }
}
