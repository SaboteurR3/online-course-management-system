package com.task.onlinecoursemanagementsystem.instructor.lesson.controller;

import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonCreateDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonGetDto;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Lesson", description = "Operations related to managing lessons for instructors.")
public interface LessonControllerApi {

    @Operation(
            summary = "Get Courses",
            description = "Fetch the list of courses. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of courses.")
    List<IdNameDto> getCourses(
            @Parameter(description = "Optional search term to filter courses by name.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Lessons",
            description = "Retrieve a paginated list of lessons, with optional filters by course and search term. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of lessons.")
    PageView<LessonGetDto> getLessons(
            @Parameter(description = "Pagination and sorting criteria for lessons.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course ID.") @RequestParam(required = false) Long courseId,
            @Parameter(description = "Optional search term to filter lessons by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Create a New Lesson",
            description = "Required role: `INSTRUCTOR`"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Lesson updated successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Lesson exists",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "course_title_start_time_must_be_unique",
                                            value = "{\"errorCode\": \"course_title_start_time_must_be_unique\"}",
                                            description = "course title start time must be unique"
                                    )
                            }
                    )
            )
    })
    void createLesson(
            @Parameter(description = "Lesson creation data.") @Valid @RequestPart LessonCreateDto data,
            @Parameter(description = "Optional list of lesson attachments.") @RequestParam(name = "attachments", required = false) List<MultipartFile> attachments
    );

    @Operation(
            summary = "Update lesson",
            description = "Required role: `INSTRUCTOR`"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Lesson updated successfully",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Lesson exists",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "course_title_start_time_must_be_unique",
                                            value = "{\"errorCode\": \"course_title_start_time_must_be_unique\"}",
                                            description = "course title start time must be unique"
                                    )
                            }
                    )
            )
    })
    void updateLesson(
            @Parameter(description = "ID of the lesson to update.") @PathVariable Long id,
            @Parameter(description = "Updated lesson data.") @Valid @RequestPart LessonUpdateDto data,
            @Parameter(description = "Optional list of lesson attachments.") @RequestParam(name = "attachments", required = false) List<MultipartFile> attachments
    );

    @Operation(
            summary = "Get Lesson Files",
            description = "Fetch files associated with a specific lesson. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of lesson files.")
    List<IdNameDto> getLessonFiles(
            @Parameter(description = "Optional filter by lesson ID.") @RequestParam(required = false) Long lessonId
    );

    @Operation(
            summary = "Download File",
            description = "Download a specific lesson file by its name. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "200", description = "File successfully downloaded.")
    ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Name of the file to download.") @PathVariable String fileName
    );

    @Operation(
            summary = "Delete Lesson",
            description = "Delete a specific lesson by its ID. Required role: `INSTRUCTOR`."
    )
    @ApiResponse(responseCode = "204", description = "Lesson successfully deleted.")
    void deleteLesson(
            @Parameter(description = "ID of the lesson to delete.") @PathVariable Long id
    );
}
