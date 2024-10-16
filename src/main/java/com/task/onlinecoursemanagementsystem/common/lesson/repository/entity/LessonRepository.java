package com.task.onlinecoursemanagementsystem.common.lesson.repository.entity;

import com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto.LessonGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("""
            SELECT
                l.id                    AS lessonId,
                l.title                 AS lessonTitle,
                l.content               AS lessonContent,
                l.durationInMinutes     AS durationInMinutes,
                l.startTime             AS startTime,
                c.title                 AS courseName,
                ci                      AS instructor
            FROM Lesson l
            JOIN FETCH Course c on c.id = l.course.id
            JOIN c.instructor ci
            WHERE (:courseId is null or c.id = :courseId)
                        AND (:search is null
                            or c.title ilike %:search%
                            or ci.firstName ilike %:search%
                            or ci.lastName ilike %:search%
                            or ci.email ilike %:search%
                            or c.description ilike %:search%)
            """)
    Page<LessonGetDto> getLessons(Pageable pageable, Long courseId, String search);
}
