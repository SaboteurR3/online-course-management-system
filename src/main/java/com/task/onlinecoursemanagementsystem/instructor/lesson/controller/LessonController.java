package com.task.onlinecoursemanagementsystem.instructor.lesson.controller;

import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.service.LessonService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonGetDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("instructor/lessons")
@RequiredArgsConstructor
public class LessonController implements LessonControllerApi{
    private final CourseService courseService;
    private final LessonService lessonService;

    @Override
    @GetMapping("courses")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<IdNameDto> getCourses(@RequestParam(required = false) String search) {
        return courseService.getCourses(search);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<LessonGetDto> getLessons(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(lessonService.getLessons(pageAndSortCriteria, courseId, search));
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(
            @RequestPart @Valid LessonCreateDto data,
            @RequestParam(name = "attachments", required = false) List<MultipartFile> attachments) {
        lessonService.createLesson(data, attachments);
    }

    @Override
    @PutMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLesson(
            @PathVariable Long id,
            @RequestPart @Valid LessonUpdateDto data,
            @RequestParam(name = "attachments", required = false) List<MultipartFile> attachments
    ) {
        lessonService.updateLesson(id, data, attachments);
    }

    @Override
    @GetMapping("files")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<IdNameDto> getLessonFiles(@RequestParam(required = false) Long lessonId) {
        return lessonService.getInstructorLessonFiles(lessonId);
    }

    @Override
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("download-file/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            InputStreamResource resource = lessonService.downloadLesson(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Override
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
