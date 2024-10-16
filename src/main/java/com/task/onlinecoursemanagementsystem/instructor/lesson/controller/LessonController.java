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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class LessonController {
    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping("courses")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<IdNameDto> getCourses(@RequestParam(required = false) String search) {
        return courseService.getCourses(search);
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<LessonGetDto> getLessons(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(lessonService.getLessons(pageAndSortCriteria, courseId, search));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(
            @RequestPart @Valid LessonCreateDto data,
            @RequestParam(name = "attachments", required = false) List<MultipartFile> attachments) {
        lessonService.createLesson(data, attachments);
    }

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

    @GetMapping("files")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<IdNameDto> getLessonFiles(@RequestParam(required = false) String search) {
        return lessonService.getLessonFiles(search);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
