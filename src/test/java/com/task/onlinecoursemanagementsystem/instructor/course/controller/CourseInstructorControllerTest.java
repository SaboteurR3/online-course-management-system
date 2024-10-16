package com.task.onlinecoursemanagementsystem.instructor.course.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.course.service.CourseService;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageViewUtil;
import com.task.onlinecoursemanagementsystem.common.review.service.ReviewService;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.ReviewGetDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseInstructorControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private CourseInstructorController controller;

    private static CourseCreateDto courseCreateDto;

    @BeforeAll
    public static void setupObjects() {
        courseCreateDto = CourseCreateDto.builder()
                .title("title")
                .description("description")
                .category(CourseCategory.SCIENCE)
                .build();
    }

    @Test
    void test_getCourseCategories() {
        CourseCategory[] categories = CourseCategory.values();
        CourseCategory[] actualCategories = controller.getCourseCategories();
        assertArrayEquals(categories, actualCategories);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,5,SCIENCE,search_1",
            "2,10,SCIENCE,null"
    }, nullValues = "null")
    void test_getCourses(int page, int size, CourseCategory category, String search) {
        PageAndSortCriteria pageAndSortCriteria = new PageAndSortCriteria(page, size, List.of("id:desc"));
        CourseGetDto courseGetDto = mock(CourseGetDto.class);
        List<CourseGetDto> courses = List.of(courseGetDto);

        when(courseService.getCourses(any(), any(), any()))
                .thenReturn(PageViewUtil.create(courses));

        PageView<CourseGetDto> actualPage = controller.getCourses(pageAndSortCriteria, category, search);
        assertEquals(1, actualPage.totalRows());
        assertEquals(courses, actualPage.results());
    }

    @Test
    void test_getCourseById() {
        Long courseId = 123L;

        CourseDetailsGetDto courseDetails = mock(CourseDetailsGetDto.class);
        when(courseService.getCourseDetailsById(courseId)).thenReturn(courseDetails);

        CourseDetailsGetDto actualDetails = controller.getCourseById(courseId);
        assertEquals(courseDetails, actualDetails);
    }

    @Test
    void test_getCourseStudents() {
        Long courseId = 123L;
        UserGetDto userGetDto = Mockito.mock(UserGetDto.class);
        List<UserGetDto> students = List.of(userGetDto);

        when(courseService.getCourseStudents(courseId)).thenReturn(students);

        List<UserGetDto> actualStudents = controller.getCourseStudents(courseId);
        assertEquals(students, actualStudents);
    }

    @Test
    void test_createCourse() {
        doNothing().when(courseService).createCourse(any());
        controller.createCourse(courseCreateDto);
        verify(courseService).createCourse(courseCreateDto);
    }

    @Test
    void test_updateCourse() {
        Long courseId = 123L;
        doNothing().when(courseService).updateCourse(any(), any());
        controller.updateCourse(courseId, courseCreateDto);
        verify(courseService).updateCourse(courseId, courseCreateDto);
    }

    @Test
    void test_deleteCourse() {
        Long courseId = 123L;

        doNothing().when(courseService).deleteCourse(any());
        controller.deleteCourse(courseId);
        verify(courseService).deleteCourse(courseId);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,5,123,5,null",
            "2,10,null,8,good",
    }, nullValues = "null")
    void test_getCourseReviews(int page, int size, Long courseId, Long rating, String search) {
        PageAndSortCriteria pageAndSortCriteria = new PageAndSortCriteria(page, size, List.of("id:asc"));
        ReviewGetDto reviewGetDto = mock(ReviewGetDto.class);
        List<ReviewGetDto> reviews = List.of(reviewGetDto);

        when(reviewService.getCourseReviews(any(), any(), any(), any()))
                .thenReturn(PageViewUtil.create(reviews));

        PageView<ReviewGetDto> actualPage = controller.getCourseReviews(pageAndSortCriteria, courseId, rating, search);
        assertEquals(1, actualPage.totalRows());
        assertEquals(reviews, actualPage.results());
    }
}
