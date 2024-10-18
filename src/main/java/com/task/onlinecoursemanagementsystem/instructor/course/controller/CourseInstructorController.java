package com.task.onlinecoursemanagementsystem.instructor.course.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.common.review.service.ReviewService;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.ReviewGetDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("instructor/courses")
@RequiredArgsConstructor
public class CourseInstructorController {
    private final ReviewService reviewService;
    private final CourseService courseService;

    @GetMapping("categories")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseCategory[] getCourseCategories() {
        return CourseCategory.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) CourseCategory category,
            @RequestParam(required = false) String search
    ) {
        return PageView.of(courseService.getCourses(pageAndSortCriteria, category, search));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseDetailsGetDto getCourseById(@PathVariable Long id) {
        return courseService.getCourseDetailsById(id);
    }

    @GetMapping("{id}/students")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<UserGetDto> getCourseStudents(@PathVariable Long id) {
        return courseService.getCourseStudents(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(
            @Valid @RequestPart CourseCreateDto data,
            @RequestParam(name = "attachment", required = false) MultipartFile attachment) {
        courseService.createCourse(data, attachment);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseCreateDto data
    ) {
        courseService.updateCourse(id, data);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("reviews")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public PageView<ReviewGetDto> getCourseReviews(
            PageAndSortCriteria pageAndSortCriteria,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) @Min(value = 1) @Max(value = 10) Long rating,
            @RequestParam(required = false) String search) {
        return PageView.of(reviewService.getCourseReviews(pageAndSortCriteria, courseId, rating, search));
    }

    @GetMapping("{courseId}/syllabus")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public IdNameDto getCourseSyllabus(@PathVariable Long courseId) {
        return courseService.getSyllabus(courseId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("download-syllabus/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            InputStreamResource resource = courseService.downloadSyllabus(fileName);
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
