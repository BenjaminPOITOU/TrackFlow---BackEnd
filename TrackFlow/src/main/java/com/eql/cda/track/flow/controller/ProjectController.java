package com.eql.cda.track.flow.controller;



import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.security.SecurityUser;
import com.eql.cda.track.flow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Handles REST requests related to projects for a specific user.
 * All endpoints are prefixed with /api/users/{userId}/projects.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Creates a new project for a specific user.
     * The authenticated principal should have the authority to create a project for the given userId.
     *
     * @param userId The ID of the user for whom the project is created.
     * @param projectCreateDto The DTO containing the project's initial data.
     * @return A ResponseEntity with status 201 (Created), a Location header pointing to the new resource,
     * and the created project data in the body.
     */
    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<ProjectViewDto> createProject(@PathVariable Long userId, @Valid @RequestBody ProjectCreateDto projectCreateDto) {
        ProjectViewDto createdProject = projectService.createProject(userId, projectCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/users/{userId}/projects")
                .buildAndExpand(createdProject.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdProject);
    }

    /**
     * Retrieves a paginated list of projects for the currently authenticated user.
     * The user is identified via the JWT principal, so no userId is needed in the path.
     *
     * @param principal The security principal of the logged-in user, injected by Spring Security.
     * @param pageable Pagination and sorting information.
     * @return A ResponseEntity containing a Page of ProjectViewDto.
     */
    @GetMapping("/projects/me")
    public ResponseEntity<Page<ProjectViewDto>> getMyProjects(
            @AuthenticationPrincipal SecurityUser principal,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProjectViewDto> projectsPage = projectService.getAllProjectsPaginated(principal.getId(), pageable);
        return ResponseEntity.ok(projectsPage);
    }

    /**
     * Retrieves the details of a specific project belonging to a user.
     * The service layer is responsible for ensuring the project belongs to the user.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project to retrieve.
     * @return A ResponseEntity containing the detailed ProjectViewDto.
     */
    @GetMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<ProjectViewDto> getProjectById(@PathVariable Long userId, @PathVariable Long projectId) {
        ProjectViewDto projectViewDto = projectService.getProjectByIdAndUser(projectId, userId);
        return ResponseEntity.ok(projectViewDto);
    }

    /**
     * Updates an existing project.
     *
     * @param userId The ID of the user who owns the project.
     * @param projectId The ID of the project to update.
     * @param projectUpdateDto The DTO containing the fields to update.
     * @return A ResponseEntity with status 204 (No Content) on successful update.
     */
    @PatchMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        projectService.updateProjectForUser(projectId, userId, projectUpdateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Archives a specific project, marking it as inactive.
     *
     * @param userId The ID of the user who owns the project.
     * @param projectId The ID of the project to archive.
     * @return A ResponseEntity with status 204 (No Content).
     */
    @PostMapping("/users/{userId}/projects/{projectId}/archive")
    public ResponseEntity<Void> archiveProject(@PathVariable Long userId, @PathVariable Long projectId) {
        projectService.archiveProjectForUser(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Permanently deletes a specific project.
     * This is a destructive operation.
     *
     * @param userId The ID of the user who owns the project.
     * @param projectId The ID of the project to delete.
     * @return A ResponseEntity with status 204 (No Content).
     */
    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long userId, @PathVariable Long projectId) {
        projectService.deleteProjectForUser(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
