package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.VersionUpdateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionViewDto;
import com.eql.cda.track.flow.service.VersionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Handles REST requests for versions, nested within a project, composition, and branch context.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/projects/{projectId}/compositions/{compositionId}/branches/{branchId}/versions")
public class VersionController {

    private final VersionService versionService;

    @Autowired
    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    /**
     * Creates a new version within a specific branch.
     * @param projectId The project context ID.
     * @param compositionId The composition context ID.
     * @param branchId The branch ID to add the version to.
     * @param versionCreateDto The DTO with creation data.
     * @return A {@link ResponseEntity} with status 201 (Created) and the created version's detailed view.
     */
    @PostMapping
    public ResponseEntity<VersionViewDto> createVersion(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId,
            @Valid @RequestBody VersionCreateDto versionCreateDto) {
        VersionViewDto createdVersion = versionService.createVersion(projectId, compositionId, branchId, versionCreateDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{versionId}")
                .buildAndExpand(createdVersion.getId()).toUri();
        return ResponseEntity.created(location).body(createdVersion);
    }

    /**
     * Retrieves all versions for a specific branch in a summary format.
     * @param projectId The project context ID.
     * @param compositionId The composition context ID.
     * @param branchId The ID of the branch.
     * @return A {@link ResponseEntity} containing a list of version summaries.
     */
    @GetMapping
    public ResponseEntity<List<VersionSummaryDto>> getVersionsByBranch(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId) {
        List<VersionSummaryDto> versions = versionService.getVersionsByBranch(projectId, compositionId, branchId);
        return ResponseEntity.ok(versions);
    }

    /**
     * GET /api/projects/{projectId}/compositions/{compositionId}/branches/{branchId}/versions/latest
     * Retrieves the most recent version of a specific branch.
     *
     * @param projectId The ID of the project.
     * @param compositionId The ID of the composition.
     * @param branchId The ID of the branch.
     * @return A {@link ResponseEntity} with status 200 OK and the version data if found,
     *         or status 204 No Content if the branch has no versions.
     */
    @GetMapping("/latest")
    public ResponseEntity<VersionViewDto> getLatestVersionByBranch(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId) {

        Optional<VersionViewDto> latestVersionOpt = versionService.getLatestVersionByBranch(projectId, compositionId, branchId);

        return latestVersionOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Retrieves a single, detailed version by its ID.
     * @param projectId The project context ID.
     * @param compositionId The composition context ID.
     * @param branchId The branch context ID.
     * @param versionId The ID of the version to retrieve.
     * @return A {@link ResponseEntity} containing the detailed version view.
     */
    @GetMapping("/{versionId}")
    public ResponseEntity<VersionViewDto> getVersionById(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId,
            @PathVariable Long versionId) {
        VersionViewDto version = versionService.getVersionById(projectId, compositionId, branchId, versionId);
        return ResponseEntity.ok(version);
    }

    /**
     * Partially updates an existing version.
     * @param projectId The project context ID.
     * @param compositionId The composition context ID.
     * @param branchId The branch context ID.
     * @param versionId The ID of the version to update.
     * @param versionUpdateDto The DTO with update data.
     * @return A {@link ResponseEntity} containing the updated version's detailed view.
     */
    @PatchMapping("/{versionId}")
    public ResponseEntity<VersionViewDto> updateVersion(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId,
            @PathVariable Long versionId,
            @Valid @RequestBody VersionUpdateDto versionUpdateDto) {
        VersionViewDto updatedVersion = versionService.updateVersion(projectId, compositionId, branchId, versionId, versionUpdateDto);
        return ResponseEntity.ok(updatedVersion);
    }

    /**
     * Deletes a version.
     * @param projectId The project context ID.
     * @param compositionId The composition context ID.
     * @param branchId The branch context ID.
     * @param versionId The ID of the version to delete.
     * @return A {@link ResponseEntity} with status 204 (No Content).
     */
    @DeleteMapping("/{versionId}")
    public ResponseEntity<Void> deleteVersion(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId,
            @PathVariable Long versionId) {
        versionService.deleteVersion(projectId, compositionId, branchId, versionId);
        return ResponseEntity.noContent().build();
    }
}