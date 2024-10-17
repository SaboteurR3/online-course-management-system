package com.task.onlinecoursemanagementsystem.student.enrollment.repository;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.student.enrollment.controller.dto.EnrollmentsGetDto;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query(
            """
                    SELECT
                        e.id                AS id,
                        c.title             AS courseTitle,
                        c.description       AS courseDescription,
                        c.id                AS courseId,
                        e.enrollmentDate    AS enrollmentDate,
                        e.progress          AS progress,
                        e.status            AS status
                    FROM Enrollment e
                    JOIN e.course c
                    WHERE e.student.id = :studentId AND e.active = :active
                    AND (:courseId is null or c.id = :courseId)
                                AND (:search is null
                                    or c.title ilike %:search%
                                    or c.description ilike %:search%)
                    """)
    Page<EnrollmentsGetDto> getEnrollments(Pageable pageable,
                                           Long courseId,
                                           Long studentId,
                                           Boolean active,
                                           String search);

    Optional<Enrollment> findByCourseAndStudent(Course course, User user);
}
