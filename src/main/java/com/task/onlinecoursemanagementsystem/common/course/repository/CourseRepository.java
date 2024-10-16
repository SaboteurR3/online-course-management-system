package com.task.onlinecoursemanagementsystem.common.course.repository;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
            SELECT
                c.id             AS id,
                c.title          AS title,
                c.description    AS description,
                c.category       AS category,
                ci               AS instructor
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

    @Query("""
            SELECT c
            FROM Course c
            LEFT JOIN FETCH c.lessons l
            WHERE c.id = :id
            """)
    Optional<Course> getCourseDetails(Long id);

    @Query("""
            SELECT
                ci               AS instructor
            FROM Course c
            JOIN c.instructor ci
            WHERE c.id = :courseId
    """)
    List<UserGetDto> getCourseStudents(Long courseId);
}
