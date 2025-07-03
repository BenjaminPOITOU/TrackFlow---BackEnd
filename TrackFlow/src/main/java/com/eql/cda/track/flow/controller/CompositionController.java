package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.service.CompositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Handles REST requests for compositions, specifically those nested within a project context.
 * The endpoints in this controller are responsible for the full lifecycle (CRUD) of a composition
 * as it relates to its parent project.
 * All endpoints are prefixed with {@code /api/projects/{projectId}/compositions}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/compositions")
@CrossOrigin(origins = "http://localhost:3000")
public class CompositionController {

    private final CompositionService compositionService;

    /**
     * Constructs the controller with its required service dependency.
     * @param compositionService The service responsible for composition business logic.
     */
    @Autowired
    public CompositionController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }

    /**
     * Creates a new composition within a specific project.
     * The service layer is responsible for associating the new composition with the given project.
     *
     * @param projectId The ID of the parent project.
     * @param compositionCreateDto The DTO containing the data for the new composition. It is validated by the framework.
     * @return A {@link ResponseEntity} with status 201 (Created), a {@code Location} header
     * pointing to the URL of the new resource, and the created composition's detailed view in the body.
     */
    @PostMapping
    public ResponseEntity<CompositionViewDto> createComposition(
            @PathVariable Long projectId,
            @Valid @RequestBody CompositionCreateDto compositionCreateDto) {
        CompositionViewDto createdComposition = compositionService.createComposition(projectId, compositionCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/projects/{projectId}/compositions/{compositionId}")
                .buildAndExpand(projectId, createdComposition.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdComposition);
    }

    /**
     * Retrieves a list of all compositions (in a summary format) for a specific project.
     *
     * @param projectId The ID of the project whose compositions are to be retrieved.
     * @return A {@link ResponseEntity} containing a list of {@link CompositionSummaryDto}.
     */
    @GetMapping
    public ResponseEntity<List<CompositionSummaryDto>> getAllCompositionsByProject(@PathVariable Long projectId) {
        List<CompositionSummaryDto> compositions = compositionService.getAllCompositionsByProject(projectId);
        return ResponseEntity.ok(compositions);
    }

    /**
     * Retrieves the detailed view of a specific composition, ensuring it belongs to the specified project.
     *
     * @param projectId The ID of the parent project, used for context and security checks in the service layer.
     * @param compositionId The ID of the composition to retrieve.
     * @return A {@link ResponseEntity} containing the {@link CompositionViewDto}.
     */
    @GetMapping("/{compositionId}")
    public ResponseEntity<CompositionViewDto> getCompositionById(
            @PathVariable Long projectId,
            @PathVariable Long compositionId) {
        CompositionViewDto composition = compositionService.getCompositionById(projectId, compositionId);
        return ResponseEntity.ok(composition);
    }

    /**
     * Partially updates a specific composition using the PATCH method.
     * Only the non-null fields in the request body will be updated.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the composition to update.
     * @param compositionUpdateDto The DTO containing the fields to update. It is validated by the framework.
     * @return A {@link ResponseEntity} containing the updated {@link CompositionViewDto}.
     */
    @PatchMapping("/{compositionId}")
    public ResponseEntity<CompositionViewDto> updateComposition(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @Valid @RequestBody CompositionUpdateDto compositionUpdateDto) {
        CompositionViewDto updatedComposition = compositionService.updateComposition(projectId, compositionId, compositionUpdateDto);
        return ResponseEntity.ok(updatedComposition);
    }

    /**
     * Deletes a specific composition from a project.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the composition to delete.
     * @return A {@link ResponseEntity} with status 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping("/{compositionId}")
    public ResponseEntity<Void> deleteComposition(
            @PathVariable Long projectId,
            @PathVariable Long compositionId) {
        compositionService.deleteComposition(projectId, compositionId);
        return ResponseEntity.noContent().build();
    }
}