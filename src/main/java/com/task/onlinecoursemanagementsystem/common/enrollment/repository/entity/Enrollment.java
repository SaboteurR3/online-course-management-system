package com.task.onlinecoursemanagementsystem.common.enrollment.repository.entity;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_enrollment")
    @SequenceGenerator(name = "seq_enrollment", sequenceName = "seq_enrollment", allocationSize = 1, initialValue = 1000)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "progress")
    private Double progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment enrollment)) return false;

        return this.getId() != null && this.getId().equals(enrollment.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
