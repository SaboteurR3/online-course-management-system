package com.task.onlinecoursemanagementsystem.common.lesson.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.LessonRepository;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto.LessonCreateDto;
import com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto.LessonGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.lesson.controller.dto.LessonUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final CourseService courseService;
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

    public void createLesson(LessonCreateDto data) {
        Course course = courseService.lookupCourse(data.courseId());
        Lesson lesson = Lesson.builder()
                .title(data.title())
                .content(data.content())
                .course(course)
                .durationInMinutes(data.durationInMinutes())
                .startTime(data.startTime())
                .build();

        try {
            repository.saveAndFlush(lesson);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_course_title_start_time", "course_title_start_time_must_be_unique"));
        }
    }

    public void updateLesson(Long id, LessonUpdateDto data) {
        Lesson lesson = lookupLesson(id);
        lesson.setTitle(data.title());
        lesson.setContent(data.content());
        lesson.setDurationInMinutes(data.durationInMinutes());
        lesson.setStartTime(data.startTime());
        try {
            repository.saveAndFlush(lesson);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_course_title_start_time", "course_title_start_time_must_be_unique"));
        }
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lookupLesson(id);
        try {
            repository.delete(lesson);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }
}
