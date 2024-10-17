package com.task.onlinecoursemanagementsystem.common.lesson.service;

import com.task.onlinecoursemanagementsystem.common.enums.AllowedFileTypes;
import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.Attachment;
import com.task.onlinecoursemanagementsystem.common.attachment.service.AttachmentService;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.LessonRepository;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonGetDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonUpdateDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final CourseService courseService;
    private final AttachmentService attachmentService;
    private final UserService userService;
    private final LessonRepository repository;

    public Lesson lookupLesson(Long id) {
        return repository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<LessonGetDto> getLessons(
            PageAndSortCriteria pageAndSortCriteria,
            Long courseId,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return repository.getLessons(pageable, courseId, search);
    }

    public void createLesson(LessonCreateDto data, List<MultipartFile> attachments) {
        validateAttachments(attachments);

        Course course = courseService.lookupCourse(data.courseId());
        Lesson lesson = Lesson.builder()
                .title(data.title())
                .content(data.content())
                .course(course)
                .durationInMinutes(data.durationInMinutes())
                .startTime(data.startTime())
                .build();

        saveAttachments(lesson, userService.curentUser(), attachments);
        try {
            repository.saveAndFlush(lesson);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_course_title_start_time", "course_title_start_time_must_be_unique"));
        }
    }

    public void updateLesson(Long id, LessonUpdateDto data, List<MultipartFile> attachments) {
        validateAttachments(attachments);

        Lesson lesson = lookupLesson(id);
        lesson.setTitle(data.title());
        lesson.setContent(data.content());
        lesson.setDurationInMinutes(data.durationInMinutes());
        lesson.setStartTime(data.startTime());

        List<Attachment> attachmentsToDelete = processExistingAttachmentsUpdate(data, lesson);
        saveAttachments(lesson, userService.curentUser(), attachments);
        try {
            repository.saveAndFlush(lesson);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_course_title_start_time", "course_title_start_time_must_be_unique"));
        }
        attachmentsToDelete.forEach(attachmentService::deleteAttachment);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lookupLesson(id);
        try {
            repository.delete(lesson);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }

    public static void validateAttachments(List<MultipartFile> attachments) {
        if (Objects.nonNull(attachments) && !attachments.isEmpty()
                && attachments.stream().anyMatch(a ->
                !AllowedFileTypes.getValues(AllowedFileTypes.values()).contains(a.getContentType()))
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void saveAttachments(
            Lesson lesson,
            User author,
            List<MultipartFile> attachments
    ) {
        if (Objects.isNull(attachments) || attachments.isEmpty()) {
            return;
        }
        lesson.getLessonAttachments().addAll(
                attachments.stream()
                        .map(a -> saveAttachment(author, a))
                        .toList()
        );
    }

    private List<Attachment> processExistingAttachmentsUpdate(LessonUpdateDto data, Lesson lesson) {
        return getAttachmentsToDelete(data.attachmentIds(), lesson.getLessonAttachments());
    }

    public List<Attachment> getAttachmentsToDelete(List<Long> remainingAttachmentIds, List<Attachment> existingAttachments) {
        List<Attachment> attachmentsToDelete;
        if (Objects.isNull(remainingAttachmentIds)
                || remainingAttachmentIds.isEmpty()
        ) {
            attachmentsToDelete = new ArrayList<>(existingAttachments);
            existingAttachments.clear();
            return attachmentsToDelete;
        }
        attachmentsToDelete = existingAttachments.stream()
                .filter(a -> !remainingAttachmentIds.contains(a.getId()))
                .collect(Collectors.toList());
        if ((existingAttachments.size() - attachmentsToDelete.size()) != remainingAttachmentIds.size()) {
            throw new SecurityViolationException();
        }
        existingAttachments.removeAll(attachmentsToDelete);
        return attachmentsToDelete;
    }

    private Attachment saveAttachment(User author, MultipartFile file) {
        return attachmentService.saveAttachment(author, file);
    }

    public List<IdNameDto> getInstructorLessonFiles(Long lessonId) {
        User user = userService.curentUser();
        return repository.getInstructorLessonFiles(user.getId(), lessonId);
    }

    public List<IdNameDto> getStudentLessonFiles() {
        User user = userService.curentUser();
        return repository.getStudentLessonFiles(user.getId());
    }

    public InputStreamResource downloadLesson(String fileName) {
        return attachmentService.getFile(fileName);
    }
}
