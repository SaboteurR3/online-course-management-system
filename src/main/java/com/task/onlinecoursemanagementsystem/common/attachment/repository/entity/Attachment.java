package com.task.onlinecoursemanagementsystem.common.attachment.repository.entity;

import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "attachment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_attachment")
    @SequenceGenerator(name = "seq_attachment", sequenceName = "seq_attachment", allocationSize = 1, initialValue = 1000)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "creation_ts")
    private LocalDateTime creationTs;

    @Setter
    @Column(name = "is_active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attachment attachment)) return false;

        return this.getId() != null && this.getId().equals(attachment.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
