package com.task.onlinecoursemanagementsystem.common.attachment.repository;

import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
