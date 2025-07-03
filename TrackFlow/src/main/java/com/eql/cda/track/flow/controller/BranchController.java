package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.branchDto.BranchCreateDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.branchDto.BranchUpdateDto;
import com.eql.cda.track.flow.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Handles REST requests for branches within the context of a specific composition and project.
 * This controller manages the lifecycle of branches (CRUD operations).
 * All endpoints are prefixed with {@code /api/projects/{projectId}/compositions/{compositionId}/branches}.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/projects/{projectId}/compositions/{compositionId}/branches")
public class BranchController {

    private final BranchService branchService;

    /**
     * Constructs the controller with its required service dependency.
     *
     * @param branchService The service responsible for branch business logic.
     */
    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * Creates a new branch for a specific composition.
     * The service layer validates that the composition belongs to the specified project.
     *
     * @param projectId The ID of the parent project (for context and validation).
     * @param compositionId The ID of the parent composition.
     * @param branchCreateDto The DTO containing the data for the new branch.
     * @return A {@link ResponseEntity} with status 201 (Created), a {@code Location} header pointing
     * to the new resource, and the created branch's summary view in the body.
     */
    @PostMapping
    public ResponseEntity<BranchSummaryDto> createBranch(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @Valid @RequestBody BranchCreateDto branchCreateDto) {
        BranchSummaryDto createdBranch = branchService.createBranch(projectId, compositionId, branchCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{branchId}")
                .buildAndExpand(createdBranch.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdBranch);
    }

    /**
     * Retrieves a list of all branches (in a summary format) for a specific composition.
     *
     * @param projectId The ID of the parent project (for context and validation).
     * @param compositionId The ID of the composition whose branches are to be retrieved.
     * @return A {@link ResponseEntity} containing a list of {@link BranchSummaryDto}.
     */
    @GetMapping
    public ResponseEntity<List<BranchSummaryDto>> getAllBranchesForComposition(
            @PathVariable Long projectId,
            @PathVariable Long compositionId) {
        List<BranchSummaryDto> branches = branchService.getAllBranchesForComposition(projectId, compositionId);
        return ResponseEntity.ok(branches);
    }


    /**
     * Partially updates an existing branch.
     * Only the fields provided in the request body will be updated.
     *
     * @param projectId The ID of the parent project (for context and validation).
     * @param compositionId The ID of the parent composition (for context and validation).
     * @param branchId The ID of the branch to update.
     * @param branchUpdateDto The DTO containing the fields to update.
     * @return A {@link ResponseEntity} with status 200 (OK) and the updated branch's summary view in the body.
     */
    @PatchMapping("/{branchId}")
    public ResponseEntity<BranchSummaryDto> updateBranch(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @PathVariable Long branchId,
            @Valid @RequestBody BranchUpdateDto branchUpdateDto) {
        BranchSummaryDto updatedBranch = branchService.updateBranch(projectId, compositionId, branchId, branchUpdateDto);
        return ResponseEntity.ok(updatedBranch);
    }
}