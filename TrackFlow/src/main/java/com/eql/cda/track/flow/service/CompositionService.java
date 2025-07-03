package com.eql.cda.track.flow.service;


import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Service interface for business logic related to compositions.
 * Methods are responsible for handling the lifecycle of compositions, including validation,
 * persistence, and ensuring that operations are performed within the correct project context
 * and with the appropriate user permissions.
 */
public interface CompositionService {

    /**
     * Creates a new composition and associates it with a specific project.
     *
     * @param projectId The ID of the project to which the composition will belong.
     * @param compositionCreateDto DTO containing the data for the new composition.
     * @return A detailed view DTO of the newly created composition.
     * @throws EntityNotFoundException if the specified project does not exist.
     * @throws AccessDeniedException if the current user is not authorized to add a composition to this project.
     */
    CompositionViewDto createComposition(Long projectId, CompositionCreateDto compositionCreateDto);

    /**
     * Retrieves a paginated list of the most recently updated compositions for the currently authenticated user.
     * The user's identity is resolved from the security context.
     *
     * @param pageable Pagination and sorting information.
     * @return A Page containing summary DTOs of recent compositions.
     */
    Page<CompositionSummaryDto> findRecentCompositionsForCurrentUser(Pageable pageable);

    /**
     * Retrieves a list of all compositions (in summary format) for a specific project.
     *
     * @param projectId The ID of the project whose compositions are to be retrieved.
     * @return A list of composition summary DTOs.
     * @throws EntityNotFoundException if the project does not exist.
     * @throws AccessDeniedException if the current user cannot access this project.
     */
    List<CompositionSummaryDto> getAllCompositionsByProject(Long projectId);

    /**
     * Retrieves the detailed view of a single composition, verifying it belongs to the specified project.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the composition to retrieve.
     * @return A detailed view DTO of the composition.
     * @throws EntityNotFoundException if the project or composition does not exist.
     * @throws AccessDeniedException if the composition does not belong to the specified project or if the user
     *         lacks permission.
     */
    CompositionViewDto getCompositionById(Long projectId, Long compositionId);

    /**
     * Updates an existing composition.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the composition to update.
     * @param compositionUpdateDto DTO containing the fields to update.
     * @return A detailed view DTO of the updated composition.
     * @throws EntityNotFoundException if the project or composition does not exist.
     * @throws AccessDeniedException if the composition does not belong to the specified project or if the user
     *         lacks permission to modify it.
     */
    CompositionViewDto updateComposition(Long projectId, Long compositionId, CompositionUpdateDto compositionUpdateDto);

    /**
     * Deletes a composition.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the composition to delete.
     * @throws EntityNotFoundException if the project or composition does not exist.
     * @throws AccessDeniedException if the composition does not belong to the specified project or if the user
     *         lacks permission to delete it.
     */
    void deleteComposition(Long projectId, Long compositionId);

}