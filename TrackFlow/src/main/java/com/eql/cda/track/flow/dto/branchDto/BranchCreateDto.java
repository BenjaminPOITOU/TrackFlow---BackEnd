package com.eql.cda.track.flow.dto.branchDto;

/**
 * A Data Transfer Object that carries the necessary information to create a new {@link com.eql.cda.track.flow.entity.Branch}.
 * It is used as the request body for branch creation endpoints.
 */
public class BranchCreateDto {

    private Long branchParentId;
    private String branchName;
    private String description;

    /**
     * Default constructor required for framework instantiation (e.g., JSON deserialization).
     */
    public BranchCreateDto() {
    }

    /**
     * Constructs a new DTO for branch creation.
     *
     * @param branchParentId The ID of the parent branch, if any. Can be null for a root branch.
     * @param branchName The name of the new branch.
     * @param description A short description of the branch's purpose.
     */
    public BranchCreateDto(Long branchParentId, String branchName, String description) {
        this.branchParentId = branchParentId;
        this.branchName = branchName;
        this.description = description;
    }

    public Long getBranchParentId() {
        return branchParentId;
    }
    public void setBranchParentId(Long branchParentId) {
        this.branchParentId = branchParentId;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}