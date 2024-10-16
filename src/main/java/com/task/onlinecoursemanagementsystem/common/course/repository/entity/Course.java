package com.task.onlinecoursemanagementsystem.common.course.repository.entity;

import com.task.onlinecoursemanagementsystem.student_module.enrollment.repository.entity.Enrollment;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.review.repository.entity.Review;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_course")
    @SequenceGenerator(name = "seq_course", sequenceName = "seq_course", allocationSize = 1, initialValue = 1000)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CourseCategory category; // add syllabus

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;

        return this.getId() != null && this.getId().equals(course.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    } // TODO why not lombok
}
