package com.task.onlinecoursemanagementsystem.common.review.repository;

import com.task.onlinecoursemanagementsystem.common.review.repository.entity.Review;
import com.task.onlinecoursemanagementsystem.student_module.review.controller.dto.ReviewGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
                SELECT
                    r.id            AS id,
                    r.comment       AS comment,
                    r.rating        AS rating,
                    r.createTs      AS createTs,
                    c.title         AS courseTitle
                FROM Review r
                JOIN r.course c
                WHERE r.student.id = :studentId
                    AND (:courseId is null or c.id = :courseId)
                    AND (:rating is null or r.rating = :rating)
                        AND (:search is null
                            or c.title ilike %:search%
                            or r.comment ilike %:search%)
            """)
    Page<ReviewGetDto> getReviews(Pageable pageable, Long courseId, Long rating, Long studentId, String search);

    @Query("""
                SELECT
                    r.id            AS id,
                    r.comment       AS comment,
                    r.rating        AS rating,
                    c.title         AS courseTitle
                FROM Review r
                JOIN r.course c
                WHERE c.instructor.id = :instructorId
                    AND (:courseId is null or c.id = :courseId)
                    AND (:rating is null or r.rating = :rating)
                        AND (:search is null
                            or c.title ilike %:search%
                            or r.comment ilike %:search%)
            """)
    Page<ReviewGetDto> getCourseReviews(Pageable pageable, Long courseId, Long rating, Long instructorId, String search);
}
