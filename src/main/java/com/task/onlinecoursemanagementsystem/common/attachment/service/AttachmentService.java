package com.task.onlinecoursemanagementsystem.common.attachment.service;

import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.Attachment;
import com.task.onlinecoursemanagementsystem.common.attachment.repository.AttachmentRepository;
import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.AttachmentType;
import com.task.onlinecoursemanagementsystem.minio.service.MinioService;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final MinioService minioService;
    private final AttachmentRepository repository;

    public Attachment saveAttachment(User author, AttachmentType attachmentType, MultipartFile file) {
        Attachment attachment = Attachment.builder()
                .name(file.getOriginalFilename())
                .type(attachmentType)
                .contentType(file.getContentType())
                .author(author)
                .creationTs(LocalDateTime.now())
                .active(true)
                .build();
        attachment = repository.saveAndFlush(attachment);
        try {
            minioService.uploadFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return attachment;
    }

    public InputStreamResource getFile(String fileName) {
        try {
            return minioService.download(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAttachment(Attachment attachment) {
        repository.deleteById(attachment.getId());
        repository.flush();
        minioService.deleteFile(attachment.getName());
    }
}
