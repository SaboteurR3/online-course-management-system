package com.task.onlinecoursemanagementsystem.student.review.controller;

import com.task.onlinecoursemanagementsystem.common.course.repository.entity.CourseCategory;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.course.controller.dto.CourseGetDto;
import com.task.onlinecoursemanagementsystem.student.review.controller.dto.CourseReviewCreateDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Student Reviews", description = "Operations related to managing course reviews for students.")
public interface ReviewControllerApi {

    @Operation(
            summary = "Get Course Categories",
            description = "Retrieve a list of course categories available for reviews. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of course categories.")
    CourseCategory[] getCourseCategories();

    @Operation(
            summary = "Get Courses",
            description = "Retrieve a paginated list of courses with optional filters for categories and search terms. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of courses.")
    PageView<CourseGetDto> getCourses(
            @Parameter(description = "Pagination and sorting criteria for courses.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course category.") @RequestParam(required = false) CourseCategory category,
            @Parameter(description = "Optional search term to filter courses by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Reviews",
            description = "Retrieve a paginated list of reviews with optional filters for course ID, rating, and search terms. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of reviews.")
    PageView<ReviewGetDto> getReviews(
            @Parameter(description = "Pagination and sorting criteria for reviews.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course ID.") @RequestParam(required = false) Long courseId,
            @Parameter(description = "Optional filter by review rating (1-10).") @RequestParam(required = false) @Min(value = 1) @Max(value = 10) Long rating,
            @Parameter(description = "Optional search term to filter reviews by content.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Create Review",
            description = "Create a new course review. Required role: `STUDENT`."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created the review.",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Course is not completed",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "course is not completed",
                                            value = "{\"errorCode\": \"course is not completed\"}",
                                            description = "course is not completed"
                                    )
                            }
                    )
            )
    })
    void createReview(
            @Parameter(description = "The review data to create.") @Valid @RequestBody CourseReviewCreateDto data
    );
}

