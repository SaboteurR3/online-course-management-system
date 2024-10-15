package com.task.onlinecoursemanagementsystem.common.review.repository.entity;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_review")
    @SequenceGenerator(name = "seq_review", sequenceName = "seq_review", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "create_ts")
    private LocalDateTime createTs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;

        return this.getId() != null && this.getId().equals(review.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
