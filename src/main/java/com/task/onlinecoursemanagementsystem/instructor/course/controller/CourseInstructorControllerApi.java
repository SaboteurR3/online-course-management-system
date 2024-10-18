package com.task.onlinecoursemanagementsystem.instructor.course.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserGetDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.ReviewGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Course Instructor", description = "Operations related to managing courses and their resources for instructors.")
public interface CourseInstructorControllerApi {

    @Operation(
            summary = "Get Course Categories",
            description = "Fetch all available course categories. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of course categories.")
    CourseCategory[] getCourseCategories();

    @Operation(
            summary = "Get Courses",
            description = "Retrieve a paginated list of courses for the instructor, with optional filters by category and search term."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of courses.")
    PageView<CourseGetDto> getCourses(
            @Parameter(description = "Pagination and sorting criteria for courses.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course category.") @RequestParam(required = false) CourseCategory category,
            @Parameter(description = "Optional search term to filter courses by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Course by ID",
            description = "Fetch detailed information of a specific course by its ID. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the course details.")
    CourseDetailsGetDto getCourseById(@Parameter(description = "ID of the course to retrieve.") @PathVariable Long id);

    @Operation(
            summary = "Get Course Students",
            description = "Fetch the list of students enrolled in a specific course by its ID. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of students.")
    List<UserGetDto> getCourseStudents(@Parameter(description = "ID of the course to retrieve students for.") @PathVariable Long id);

    @Operation(
            summary = "Create a New Course",
            description = "Required role: `INSTRUCTOR`"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Course created successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "course exists",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "title_exists",
                                            value = "{\"errorCode\": \"title_exists\"}",
                                            description = "course with this title exists"
                                    )
                            }
                    )
            )
    })
    void createCourse(
            @Parameter(description = "Course creation data.") @Valid @RequestPart CourseCreateDto data,
            @Parameter(description = "Optional course attachment.") @RequestParam(name = "attachment", required = false) MultipartFile attachment);

    @Operation(
            summary = "Update Course",
            description = "Required role: `INSTRUCTOR`"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Course updated successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "course exists",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "title_exists",
                                            value = "{\"errorCode\": \"title_exists\"}",
                                            description = "course with this title exists"
                                    )
                            }
                    )
            )
    })
    void updateCourse(
            @Parameter(description = "ID of the course to update.") @PathVariable Long id,
            @Parameter(description = "Updated course data.") @Valid @RequestBody CourseCreateDto data
    );

    @Operation(
            summary = "Delete Course",
            description = "Delete a specific course by its ID. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "204", description = "Course successfully deleted.")
    void deleteCourse(@Parameter(description = "ID of the course to delete.") @PathVariable Long id);

    @Operation(
            summary = "Get Course Reviews",
            description = "Retrieve paginated reviews for a specific course with optional filters for rating and search. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of reviews.")
    PageView<ReviewGetDto> getCourseReviews(
            @Parameter(description = "Pagination and sorting criteria for reviews.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course ID.") @RequestParam(required = false) Long courseId,
            @Parameter(description = "Optional filter by rating, between 1 and 10.") @RequestParam(required = false) @Min(value = 1) @Max(value = 10) Long rating,
            @Parameter(description = "Optional search term to filter reviews by content.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Course Syllabus",
            description = "Fetch the syllabus of a specific course by its ID. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the course syllabus.")
    IdNameDto getCourseSyllabus(@Parameter(description = "ID of the course to retrieve the syllabus for.") @PathVariable Long courseId);

    @Operation(
            summary = "Download File",
            description = "Download an attachment file by its name. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "File successfully downloaded.")
    ResponseEntity<Resource> downloadFile(@Parameter(description = "Name of the file to download.") @PathVariable String fileName);
}
