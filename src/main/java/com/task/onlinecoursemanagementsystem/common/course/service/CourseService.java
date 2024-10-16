package com.task.onlinecoursemanagementsystem.common.course.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.CourseRepository;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.instructor_module.course.controller.dto.CourseLessonsGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public List<IdNameDto> getCourses(String search) {
        return repository.getCourses(search);
    }

    public Page<CourseGetDto> getCourses(
            PageAndSortCriteria pageAndSortCriteria,
            CourseCategory category,
            String search) {
        Pageable pageable = pageAndSortCriteria.build("id");
        return repository.getCourses(pageable, category, search);
    }

    public CourseDetailsGetDto getCourseDetailsById(Long id) {
        return repository.getCourseDetails(id).map(course -> {
            User instructor = course.getInstructor();

            List<CourseLessonsGetDto> lessonDtos = mapLessonsToDto(course.getLessons());

            return CourseDetailsGetDto.builder()
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .category(course.getCategory())
                    .instructorName(String.join(" ", instructor.getFirstName(), instructor.getLastName()))
                    .instructorEmail(instructor.getEmail())
                    .lessons(lessonDtos)
                    .students(course.getStudents().stream()
                            .map(user -> String.format("%s %s", user.getFirstName(), user.getLastName()))
                            .toList())
                    .build();
        }).orElseThrow(SecurityViolationException::new);
    }

    private List<CourseLessonsGetDto> mapLessonsToDto(List<Lesson> lessons) {
        return lessons.stream()
                .map(lesson -> CourseLessonsGetDto.builder()
                        .title(lesson.getTitle())
                        .content(lesson.getContent())
                        .duration(lesson.getDurationInMinutes())
                        .startTime(lesson.getStartTime())
                        .build())
                .toList();
    }

    public void createCourse(CourseCreateDto data) {
        User currentUser = userService.curentUser();
        Course course = Course.builder()
                .title(data.title())
                .description(data.description())
                .category(data.category())
                .instructor(currentUser)
                .students(new HashSet<>())
                .lessons(new ArrayList<>())
                .enrollments(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();

        try {
            repository.saveAndFlush(course);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of("uq_title", "title_exists"));
        }
    }

    public void updateCourse(Long id, CourseCreateDto data) {
        Course course = lookupCourse(id);
        course.setTitle(data.title());
        course.setDescription(data.description());
        course.setCategory(data.category());

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
