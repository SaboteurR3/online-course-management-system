package com.task.onlinecoursemanagementsystem.common.course.service;

import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.Attachment;
import com.task.onlinecoursemanagementsystem.common.attachment.repository.entity.AttachmentType;
import com.task.onlinecoursemanagementsystem.common.attachment.service.AttachmentService;
import com.task.onlinecoursemanagementsystem.common.course.repository.CourseRepository;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.enums.AllowedFileTypes;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseLessonsGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import com.task.onlinecoursemanagementsystem.student.enrollment.repository.entity.Enrollment;
import com.task.onlinecoursemanagementsystem.student.enrollment.service.EnrollmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CourseService {
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final EnrollmentService enrollmentService;
    private final CourseRepository repository;

    public Course lookupCourse(Long id) {
        return repository.findById(id).orElseThrow(SecurityViolationException::new);
    }

    public Course findCourseForUpdate(Long id) {
        return repository.findCourseForUpdate(id).orElseThrow(SecurityViolationException::new);
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

    public List<UserGetDto> getCourseStudents(Long courseId) {
        return repository.getCourseStudents(courseId);
    }

    public CourseDetailsGetDto getCourseDetailsById(Long id) {
        return repository.getCourseDetails(id).map(course -> {
            User instructor = course.getInstructor();

            List<CourseLessonsGetDto> lessonDtos = mapLessonsToDto(course.getLessons());

            return CourseDetailsGetDto.builder()
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .category(course.getCategory())
                    .maxCapacity(course.getMaxCapacity())
                    .currentCapacity(course.getCurrentCapacity())
                    .instructorName(String.join(" ", instructor.getFirstName(), instructor.getLastName()))
                    .instructorEmail(instructor.getEmail())
                    .lessons(lessonDtos)
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

    public void createCourse(CourseCreateDto data, MultipartFile attachment) {
        validateAttachment(attachment);

        User author = userService.curentUser();
        Course course = Course.builder()
                .title(data.title())
                .description(data.description())
                .category(data.category())
                .instructor(author)
                .students(new HashSet<>())
                .lessons(new ArrayList<>())
                .enrollments(new ArrayList<>())
                .reviews(new ArrayList<>())
                .maxCapacity(data.maxCapacity())
                .currentCapacity(0)
                .syllabus(saveAttachment(author, attachment))
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
        course.setMaxCapacity(data.maxCapacity());

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

    private Attachment saveAttachment(User author, MultipartFile file) {
        return attachmentService.saveAttachment(author, AttachmentType.SYLLABUS, file);
    }

    public static void validateAttachment(MultipartFile attachment) {
        if (Objects.nonNull(attachment) && !attachment.isEmpty() &&
                !AllowedFileTypes.getValues(AllowedFileTypes.values()).contains(attachment.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public IdNameDto getSyllabus(Long courseId) {
        return repository.getCourseSyllabus(courseId);
    }

    public InputStreamResource downloadSyllabus(String fileName) {
        return attachmentService.getFile(fileName);
    }

    public IdNameDto getCourseSyllabusForStudent(Long courseId) {
        checkStudentEnrollment(courseId);
        return repository.getCourseSyllabus(courseId);
    }

    public InputStreamResource downloadSyllabusForStudent(Long courseId, String fileName) {
        checkStudentEnrollment(courseId);
        return attachmentService.getFile(fileName);
    }

    private void checkStudentEnrollment(Long courseId) {
        Course course = lookupCourse(courseId);
        User user = userService.curentUser();
        Enrollment enrollmentByCourseAndUser = enrollmentService.findEnrollementByCourseAndUser(course, user);
        if (enrollmentByCourseAndUser == null) {
            throw new SecurityViolationException();
        }
    }
}
