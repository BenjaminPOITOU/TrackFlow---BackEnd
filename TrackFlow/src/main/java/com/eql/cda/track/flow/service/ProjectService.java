package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

/**
 * Service interface for business logic related to projects.
 * Methods that modify or access user-specific data must include checks
 * to ensure the authenticated user has the appropriate permissions.
 */
public interface ProjectService {

    /**
     * Creates a new project for a specified user.
     *
     * @param userId The ID of the user creating the project.
     * @param projectCreateDto The data for the new project.
     * @return A DTO representing the newly created project.
     * @throws EntityNotFoundException if the user with the given ID does not exist.
     */
    ProjectViewDto createProject(Long userId, ProjectCreateDto projectCreateDto);

    /**
     * Retrieves a specific project by its ID, ensuring the requesting user has access.
     *
     * @param projectId The ID of the project to retrieve.
     * @param userId The ID of the user making the request, used for permission checks.
     * @return A DTO view of the project.
     * @throws EntityNotFoundException if the project does not exist.
     * @throws AccessDeniedException if the specified user does not have access to the project.
     */
    ProjectViewDto getProjectByIdAndUser(Long projectId, Long userId);

    /**
     * Retrieves a paginated list of all projects visible to a specified user.
     *
     * @param userId The ID of the user whose projects are being requested.
     * @param pageable Pagination and sorting information.
     * @return A Page containing project view DTOs.
     * @throws EntityNotFoundException if the user does not exist.
     */
    Page<ProjectViewDto> getAllProjectsPaginated(Long userId, Pageable pageable);

    /**
     * Retrieves a paginated list of recent projects for the currently authenticated user.
     * The user is identified via the security context.
     *
     * @param pageable Pagination and sorting information.
     * @return A Page containing summary DTOs of recent projects.
     * @throws org.springframework.security.core.AuthenticationException if no user is authenticated.
     */
    Page<ProjectSummaryDto> findRecentProjectsForCurrentUser(Pageable pageable);

    /**
     * Updates an existing project after verifying user permissions.
     *
     * @param projectId The ID of the project to update.
     * @param userId The ID of the user making the request.
     * @param projectUpdateDto The data to update.
     * @throws EntityNotFoundException if the project does not exist.
     * @throws AccessDeniedException if the user is not allowed to modify this project.
     */
    void updateProjectForUser(Long projectId, Long userId, ProjectUpdateDto projectUpdateDto);

    /**
     * Archives a project after verifying user permissions.
     * Archiving is a logical operation, typically setting a status.
     *
     * @param projectId The ID of the project to archive.
     * @param userId The ID of the user making the request.
     * @throws EntityNotFoundException if the project does not exist.
     * @throws AccessDeniedException if the user is not allowed to archive this project.
     * @throws IllegalStateException if the project cannot be archived (e.g., already archived).
     */
    void archiveProjectForUser(Long projectId, Long userId);

    /**
     * Permanently deletes a project after verifying user permissions.
     *
     * @param projectId The ID of the project to delete.
     * @param userId The ID of the user making the request.
     * @throws EntityNotFoundException if the project does not exist.
     * @throws AccessDeniedException if the user is not allowed to delete this project.
     */
    void deleteProjectForUser(Long projectId, Long userId);
}