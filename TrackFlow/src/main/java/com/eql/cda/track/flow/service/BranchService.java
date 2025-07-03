package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.branchDto.BranchCreateDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.branchDto.BranchUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

/**
 * Service interface for business logic related to branches.
 * Methods are responsible for handling the lifecycle of branches, ensuring that operations
 * are performed within the correct project and composition context.
 */
public interface BranchService {

    /**
     * Creates a new branch and associates it with a specific composition.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the parent composition.
     * @param branchCreateDto DTO containing the data for the new branch.
     * @return A summary view DTO of the newly created branch.
     * @throws EntityNotFoundException if the specified project or composition does not exist.
     * @throws AccessDeniedException if the composition does not belong to the project.
     */
    BranchSummaryDto createBranch(Long projectId, Long compositionId, BranchCreateDto branchCreateDto);

    /**
     * Retrieves a list of all branches for a specific composition.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the composition whose branches are to be retrieved.
     * @return A list of branch summary DTOs.
     * @throws EntityNotFoundException if the specified project or composition does not exist.
     * @throws AccessDeniedException if the composition does not belong to the project.
     */
    List<BranchSummaryDto> getAllBranchesForComposition(Long projectId, Long compositionId);


    /**
     * Updates an existing branch.
     *
     * @param projectId The ID of the parent project, used for validation.
     * @param compositionId The ID of the parent composition, used for validation.
     * @param branchId The ID of the branch to update.
     * @param branchUpdateDto DTO containing the fields to update.
     * @return A summary view DTO of the updated branch.
     * @throws EntityNotFoundException if the specified project, composition, or branch does not exist.
     * @throws AccessDeniedException if the branch does not belong to the specified composition and project.
     */
    BranchSummaryDto updateBranch(Long projectId, Long compositionId, Long branchId, BranchUpdateDto branchUpdateDto);

}