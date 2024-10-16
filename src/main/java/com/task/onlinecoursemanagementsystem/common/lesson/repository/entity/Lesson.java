package com.task.onlinecoursemanagementsystem.common.lesson.repository.entity;

import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.Attachment;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lesson")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lesson")
    @SequenceGenerator(name = "seq_lesson", sequenceName = "seq_lesson", allocationSize = 1, initialValue = 1000)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Builder.Default // TODO ?
    @JoinTable(
            name = "lesson_attachments",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Attachment> LessonAttachments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson lesson)) return false;

        return this.getId() != null && this.getId().equals(lesson.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
