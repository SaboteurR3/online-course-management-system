package com.task.onlinecoursemanagementsystem.common.course.repository;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
            SELECT
                c.id                AS id,
                c.title             AS title,
                c.description       AS description,
                c.category          AS category,
                c.maxCapacity       AS maxCapacity,
                c.currentCapacity   AS currentCapacity,
                ci                  AS instructor
            FROM Course c
            JOIN c.instructor ci
            WHERE (:category is null or c.category = :category)
                        AND (:search is null
                            or c.title ilike %:search%
                            or ci.firstName ilike %:search%
                            or ci.lastName ilike %:search%
                            or ci.email ilike %:search%
                            or c.description ilike %:search%)
            """)
    Page<CourseGetDto> getCourses(Pageable pageable, CourseCategory category, String search);

    @Query("""
            SELECT
                c.id             AS id,
                c.title          AS name
            FROM Course c
            JOIN c.instructor ci
            WHERE (:search is null or c.title ilike %:search%)
            """)
    List<IdNameDto> getCourses(String search);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM Course c
            WHERE c.id = :id
            """)
    Optional<Course> findCourseForUpdate(Long id);

    @Query("""
            SELECT c
            FROM Course c
            LEFT JOIN FETCH c.lessons l
            LEFT JOIN FETCH c.instructor
            WHERE c.id = :id
            """)
    Optional<Course> getCourseDetails(Long id);

    @Query("""
            SELECT
                s.firstName    AS firstName,
                s.lastName     AS lastName,
                s.email        AS email
            FROM Course c
            JOIN c.students s
            WHERE c.id = :courseId
    """)
    List<UserGetDto> getCourseStudents(Long courseId);

    @Query("""
        SELECT
            cs.id        as id,
            cs.name      as name
        FROM Course c
        JOIN c.syllabus cs
        WHERE c.id = :courseId
    """)
    IdNameDto getCourseSyllabus(Long courseId);
}
