package com.task.onlinecoursemanagementsystem.student.lesson.controller;

import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.service.LessonService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("student/lessons")
@RequiredArgsConstructor
public class LessonStudentController {
    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping("courses")
    @PreAuthorize("hasRole('STUDENT')")
    public List<IdNameDto> getCourses(@RequestParam(required = false) String search) {
        return courseService.getCourses(search);
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public PageView<LessonGetDto> getLessons(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(lessonService.getLessons(pageAndSortCriteria, courseId, search));
    }

    @GetMapping("files")
    @PreAuthorize("hasRole('STUDENT')")
    public List<IdNameDto> getStudentLessonFiles() {
        return lessonService.getStudentLessonFiles();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("download/{fileName}")
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
}
