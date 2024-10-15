package com.task.onlinecoursemanagementsystem.common.course.repository;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
            SELECT
                c.id             AS id,
                c.title          AS title,
                c.description    AS description,
                c.category       AS category,
                c.instructor     AS instructor
            FROM Course c
            WHERE (:category is null or c.category = :category)
                        AND (:search is null
                            or c.title ilike %:search%
                            or c.description ilike %:search%)
            """)
    Page<CourseGetDto> getCourses(Pageable pageable, CourseCategory category, String search);
}
