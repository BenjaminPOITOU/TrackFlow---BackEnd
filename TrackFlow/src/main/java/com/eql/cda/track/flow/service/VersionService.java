package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.VersionUpdateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionViewDto;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing versions within a composition's branch.
 * It handles creation, retrieval, updates, and deletion of versions.
 */
public interface VersionService {

    /**
     * Creates a new version within a specified branch.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the branch to add the version to.
     * @param versionCreateDto DTO containing the data for the new version.
     * @return A DTO representing the newly created version.
     */
    VersionViewDto createVersion(Long projectId, Long compositionId, Long branchId, VersionCreateDto versionCreateDto);

    /**
     * Retrieves a list of all versions within a specific branch, in summary format.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the branch whose versions are to be retrieved.
     * @return A list of {@link VersionSummaryDto} objects.
     */
    List<VersionSummaryDto> getVersionsByBranch(Long projectId, Long compositionId, Long branchId);

    /**
     * Retrieves a single version by its ID, with detailed information.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the parent branch.
     * @param versionId The ID of the version to retrieve.
     * @return A {@link VersionViewDto} with the version's details.
     */
    VersionViewDto getVersionById(Long projectId, Long compositionId, Long branchId, Long versionId);

    /**
     * Retrieves the most recent version from a specific branch.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the branch to search within.
     * @return An {@link Optional} containing the {@link VersionViewDto} if a version exists, or an empty Optional if the branch is empty.
     */
    Optional<VersionViewDto> getLatestVersionByBranch(Long projectId, Long compositionId, Long branchId);

    /**
     * Updates an existing version with new data.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the parent branch.
     * @param versionId The ID of the version to update.
     * @param versionUpdateDto DTO containing the fields to update.
     * @return A DTO representing the updated version.
     */
    VersionViewDto updateVersion(Long projectId, Long compositionId, Long branchId, Long versionId, VersionUpdateDto versionUpdateDto);

    /**
     * Deletes a version by its ID.
     *
     * @param projectId The ID of the parent project.
     * @param compositionId The ID of the parent composition.
     * @param branchId The ID of the parent branch.
     * @param versionId The ID of the version to delete.
     */
    void deleteVersion(Long projectId, Long compositionId, Long branchId, Long versionId);
}