package com.task.onlinecoursemanagementsystem.student.lesson.controller;

import com.task.onlinecoursemanagementsystem.common.dto.IdNameDto;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageAndSortCriteria;
import com.task.onlinecoursemanagementsystem.common.paginationandsort.PageView;
import com.task.onlinecoursemanagementsystem.instructor.lesson.controller.dto.LessonGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Student Lessons", description = "Operations related to managing lessons for students.")
public interface LessonStudentControllerApi {

    @Operation(
            summary = "Get Courses",
            description = "Retrieve a list of courses for students with an optional search filter. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of courses.")
    List<IdNameDto> getCourses(
            @Parameter(description = "Optional search term to filter courses by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Lessons",
            description = "Retrieve a paginated list of lessons for students, with optional filters by course ID and search term."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of lessons.")
    PageView<LessonGetDto> getLessons(
            @Parameter(description = "Pagination and sorting criteria for lessons.") PageAndSortCriteria pageAndSortCriteria,
            @Parameter(description = "Optional filter by course ID.") @RequestParam(required = false) Long courseId,
            @Parameter(description = "Optional search term to filter lessons by name or description.") @RequestParam(required = false) String search
    );

    @Operation(
            summary = "Get Student Lesson Files",
            description = "Retrieve a list of lesson files available to students. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of lesson files.")
    List<IdNameDto> getStudentLessonFiles();

    @Operation(
            summary = "Download Lesson File",
            description = "Download a lesson file by its name. Required role: `STUDENT`."
    )
    @ApiResponse(responseCode = "200", description = "File successfully downloaded.")
    ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Name of the lesson file to download.") @PathVariable String fileName
    );
}
