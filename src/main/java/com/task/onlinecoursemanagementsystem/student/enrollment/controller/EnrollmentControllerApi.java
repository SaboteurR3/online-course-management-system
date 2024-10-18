package com.task.onlinecoursemanagementsystem.student.enrollment.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseDetailsGetDto;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.student.enrollment.controller.dto.EnrollmentsGetDto;
import com.task.onlinecoursemanagementsystem.student.enrollment.controller.dto.ProgressUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Student Enrollment", description = "Operations related to managing course enrollments for students.")
public interface EnrollmentControllerApi {

    @Operation(
            summary = "Get Course Categories",
            description = "Fetch all available course categories. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of course categories.")
    CourseCategory[] getCourseCategories();

    @Operation(
            summary = "Get Courses",
            description = "Retrieve a paginated list of courses for the student, with optional filters by category and search term."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of courses.")
    PageView<CourseGetDto> getCourses(
            @Parameter(description = "Pagination and sorting criteria for courses.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course category.") @RequestParam(required = false) CourseCategory category,
            @Parameter(description = "Optional search term to filter courses by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Course by ID",
            description = "Fetch detailed information of a specific course by its ID. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the course details.")
    CourseDetailsGetDto getCourseById(@Parameter(description = "ID of the course to retrieve.") @PathVariable Long id);

    @Operation(
            summary = "Get Course Syllabus",
            description = "Fetch the syllabus of a specific course by its ID. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the course syllabus.")
    IdNameDto getCourseSyllabusForStudent(@Parameter(description = "ID of the course to retrieve the syllabus for.") @PathVariable Long courseId);

    @Operation(
            summary = "Download Course Syllabus",
            description = "Download the syllabus file for a specific course. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "File successfully downloaded.")
    ResponseEntity<Resource> downloadFile(
            @Parameter(description = "ID of the course for which the syllabus is being downloaded.") @PathVariable Long courseId,
            @Parameter(description = "Name of the syllabus file to download.") @PathVariable String fileName
    );

    @Operation(
            summary = "Get Enrollments",
            description = "Retrieve a paginated list of enrollments for the student, with optional filters by course ID and search term."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of enrollments.")
    PageView<EnrollmentsGetDto> getEnrollments(
            @Parameter(description = "Pagination and sorting criteria for enrollments.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course ID.") @RequestParam(required = false) Long courseId,
            @Parameter(description = "Optional search term to filter enrollments.") @RequestParam(required = false) String search
    );


    @Operation(
            summary = "Enroll Student",
            description = "Enroll a student in a course. Required role: `STUDENT`."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Student enrolled successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Student already enrolled",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "student_already_enrolled_in_current_course",
                                            value = "{\"errorCode\": \"student_already_enrolled_in_current_course\"}",
                                            description = "student already enrolled in current course"
                                    )
                            }
                    )
            )
    })
    void enrollStudent(@Parameter(description = "ID of the course to enroll in.") @RequestParam Long courseId);

    @Operation(
            summary = "Update Progress",
            description = "Update the progress of an enrollment. Required role: `STUDENT`."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Progress Updated successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "conflict",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "you are not enrolled in this course.",
                                            value = "{\"errorCode\": \"you are not enrolled in this course.\"}",
                                            description = "you are not enrolled in this course."
                                    ),
                                    @ExampleObject(
                                            name = "course is already completed",
                                            value = "{\"errorCode\": \"course is already completed\"}",
                                            description = "course is already completed"
                                    )
                            }
                    )
            )
    })
    void updateProgress(
            @Parameter(description = "ID of the enrollment to update progress for.") @PathVariable Long id,
            @Parameter(description = "Progress update data.") @Valid @RequestBody ProgressUpdateDto progressUpdateDto
    );

    @Operation(
            summary = "Unenroll from Course",
            description = "Unenroll a student from a course. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "204", description = "Student successfully unenrolled from the course.")
    void unenrollFromCourse(@Parameter(description = "ID of the enrollment to unenroll from.") @PathVariable Long id);
}
