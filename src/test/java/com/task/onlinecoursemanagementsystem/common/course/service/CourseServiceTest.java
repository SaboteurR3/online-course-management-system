package com.task.onlinecoursemanagementsystem.common.course.service;

import com.task.onlinecoursemanagementsystem.common.course.repository.CourseRepository;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.Course;
import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.lesson.repository.entity.Lesson;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.security.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    UserService userService;

    @Mock
    CourseRepository repository;

    @InjectMocks
    CourseService courseService;

    private static CourseCreateDto courseCreateDto;
    private static CourseCreateDto courseUpdateDto;

    @BeforeAll
    public static void setupObjects() {
        courseCreateDto = CourseCreateDto.builder()
                .title("New Course")
                .description("Course Description")
                .category(CourseCategory.SCIENCE)
                .build();

        courseUpdateDto = CourseCreateDto.builder()
                .title("Updated Course")
                .description("Updated Description")
                .category(CourseCategory.SCIENCE)
                .build();
    }

    @Nested
    class TestLookupCourse {

        @Test
        void test_whenCourseNotExists() {
            Long id = 1000L;

            when(repository.findById(eq(id))).thenReturn(Optional.empty());
            assertThrows(SecurityViolationException.class, () -> courseService.lookupCourse(id));
        }

        @Test
        void test_whenCourseExists() {
            Long id = 1001L;
            Course course = Course.builder()
                    .id(id)
                    .title("test_course")
                    .build();

            when(repository.findById(eq(id))).thenReturn(Optional.of(course));

            Course actualCourse = courseService.lookupCourse(id);
            assertEquals(course, actualCourse);
        }
    }

    @Test
    void testGetCourses() {
        String search = "test_search";

        IdNameDto idNameDto = mock(IdNameDto.class);
        List<IdNameDto> expectedCourses = List.of(idNameDto);
        when(repository.getCourses(eq(search))).thenReturn(expectedCourses);

        List<IdNameDto> result = courseService.getCourses(search);
        assertEquals(expectedCourses, result);
    }

    @Test
    void testGetCoursesWithPagination() {
        PageAndSortCriteria pageAndSortCriteria = new PageAndSortCriteria(1, 10, List.of("id:desc"));
        CourseCategory category = CourseCategory.SCIENCE;
        String search = "test_search";

        Page<CourseGetDto> expectedPage = mock(Page.class);
        when(repository.getCourses(eq(pageAndSortCriteria.build("id")), eq(category), eq(search)))
                .thenReturn(expectedPage);

        Page<CourseGetDto> result = courseService.getCourses(pageAndSortCriteria, category, search);
        assertEquals(expectedPage, result);
    }

    @Test
    void testGetCourseStudents() {
        Long courseId = 1L;

        UserGetDto userGetDto = mock(UserGetDto.class);
        List<UserGetDto> expectedStudents = List.of(userGetDto);
        when(repository.getCourseStudents(eq(courseId))).thenReturn(expectedStudents);

        List<UserGetDto> result = courseService.getCourseStudents(courseId);
        assertEquals(expectedStudents, result);
    }

    @Nested
    class TestGetCourseDetailsById {

        @Test
        void test_whenCourseNotExists() {
            Long id = 1000L;

            when(repository.getCourseDetails(eq(id))).thenReturn(Optional.empty());
            assertThrows(SecurityViolationException.class, () -> courseService.getCourseDetailsById(id));
        }

        @Test
        void test_whenCourseExists() {
            Long id = 1001L;
            User instructor = mock(User.class);
            Lesson lesson = mock(Lesson.class);
            List<Lesson> lessons = List.of(lesson);
            Course course = Course.builder()
                    .id(id)
                    .title("Test Course")
                    .description("Course Description")
                    .category(CourseCategory.SCIENCE)
                    .instructor(instructor)
                    .lessons(lessons)
                    .build();

            when(repository.getCourseDetails(eq(id))).thenReturn(Optional.of(course));

            CourseDetailsGetDto actualDetails = courseService.getCourseDetailsById(id);

            assertEquals(course.getTitle(), actualDetails.title());
            assertEquals(course.getDescription(), actualDetails.description());
            assertEquals(course.getCategory(), actualDetails.category());
            assertEquals(instructor.getFirstName() + " " + instructor.getLastName(), actualDetails.instructorName());
            assertEquals(instructor.getEmail(), actualDetails.instructorEmail());
            assertEquals(1, actualDetails.lessons().size());
            assertEquals(lessons.get(0).getTitle(), actualDetails.lessons().get(0).title());
        }
    }

    @Nested
    class TestCreateCourse {

        @Test
        void test_createCourse() {
            User currentUser = mock(User.class);

            when(userService.curentUser()).thenReturn(currentUser);
            courseService.createCourse(courseCreateDto);

            ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
            verify(repository).saveAndFlush(courseCaptor.capture());

            Course savedCourse = courseCaptor.getValue();
            assertEquals("New Course", savedCourse.getTitle());
            assertEquals("Course Description", savedCourse.getDescription());
            assertEquals(CourseCategory.SCIENCE, savedCourse.getCategory());
            assertEquals(currentUser, savedCourse.getInstructor());
        }

        @Test
        void test_whenTitleExists() {
            when(userService.curentUser()).thenReturn(new User());

            doThrow(new RuntimeException("test: uq_title")).when(repository).saveAndFlush(any());

            assertThatThrownBy(() -> courseService.createCourse(courseCreateDto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("title_exists");
        }
    }

    @Nested
    class TestUpdateCourse {

        @Test
        void test_whenCourseNotExists() {
            Long id = 1000L;

            when(repository.findById(eq(id))).thenReturn(Optional.empty());
            assertThrows(SecurityViolationException.class, () -> courseService.updateCourse(id, courseUpdateDto));
        }

        @Test
        void test_whenUpdateSuccessful() {
            Long id = 1001L;
            Course existingCourse = Course.builder().id(id).title("Old Course").build();

            when(repository.findById(eq(id))).thenReturn(Optional.of(existingCourse));

            courseService.updateCourse(id, courseUpdateDto);
            assertEquals("Updated Course", existingCourse.getTitle());
            assertEquals("Updated Description", existingCourse.getDescription());
            assertEquals(CourseCategory.SCIENCE, existingCourse.getCategory());
        }

        @Test
        void test_whenTitleExists() {
            Long id = 1001L;
            Course existingCourse = Course.builder().id(id).title("Old Course").build();

            when(repository.findById(eq(id))).thenReturn(Optional.of(existingCourse));
            doThrow(new RuntimeException("test: uq_title")).when(repository).saveAndFlush(any());

            assertThatThrownBy(() -> courseService.updateCourse(id, courseUpdateDto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("title_exists");
        }
    }

    @Nested
    class TestDeleteCourse {

        @Test
        void test_whenCourseNotExists() {
            Long id = 1000L;

            when(repository.findById(eq(id))).thenReturn(Optional.empty());

            assertThrows(SecurityViolationException.class, () -> courseService.deleteCourse(id));
        }

        @Test
        void test_whenDeleteSuccessful() {
            Long id = 1001L;
            Course courseToDelete = Course.builder().id(id).build();

            when(repository.findById(eq(id))).thenReturn(Optional.of(courseToDelete));

            courseService.deleteCourse(id);
            verify(repository).delete(courseToDelete);
        }

        @Test
        void test_whenDataIntegrityViolationException() {
            Long id = 1001L;
            Course courseToDelete = Course.builder().id(id).build();

            when(repository.findById(eq(id))).thenReturn(Optional.of(courseToDelete));
            doThrow(DataIntegrityViolationException.class).when(repository).delete(any());

            assertThatThrownBy(() -> courseService.deleteCourse(id))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("cant_delete");
        }
    }
}
