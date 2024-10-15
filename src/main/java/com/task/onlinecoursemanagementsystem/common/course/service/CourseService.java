package com.task.onlinecoursemanagementsystem.common.course.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.CourseRepository;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final UserService userService;
    private final CourseRepository repository;

    public Course lookupCourse(Long id) {
        return repository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Page<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            CourseCategory category,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return repository.getCourses(pageable, category, search); // get only instructor name or it will loop :))))
    }

    public CourseDetailsGetDto getCourseDetailsById(Long id) {
        Course course = lookupCourse(id);
        return CourseDetailsGetDto.builder()
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .instructorName(course.getInstructor())
                .lessons(course.getLessons())
                .students(course.getStudents())
                .build();
    }

    public void createCourse(CourseCreateDto dto) {
        User currentUser = userService.curentUser();
        Course course = Course.builder()
                .title(dto.title())
                .description(dto.description())
                .category(dto.category())
                .instructor(currentUser)
                .students(new ArrayList<>())
                .lessons(new ArrayList<>())
                .enrollments(new ArrayList<>())
                .reviews(new ArrayList<>())
                .createTs(LocalDateTime.now())
                .build();

        try {
            repository.saveAndFlush(course);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_title", "title_exists"));
        }
    }

    public void updateCourse(Long id, CourseCreateDto dto) {
        Course course = lookupCourse(id);
        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setCategory(dto.category());

        try {
            repository.saveAndFlush(course);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_title", "title_exists"));
        }
    }

    public void deleteCourse(Long id) {
        Course course = lookupCourse(id);
        try {
            repository.delete(course);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("cant_delete");
        }
    }
}
