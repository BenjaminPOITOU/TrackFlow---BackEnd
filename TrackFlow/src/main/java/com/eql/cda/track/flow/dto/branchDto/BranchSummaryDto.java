package com.eql.cda.track.flow.dto.branchDto;

/**
 * A Data Transfer Object that provides a compact, read-only representation of a {@link com.eql.cda.track.flow.entity.Branch}.
 * It is designed to be sent to the client in lists or summary views.
 */
public class BranchSummaryDto {

    private Long id;
    private Long parentBrancheId;
    private String name;
    private String description;

    /**
     * Default constructor required for framework instantiation.
     */
    public BranchSummaryDto() {
    }

    /**
     * Constructs a complete summary DTO for a branch.
     *
     * @param id The unique identifier of the branch.
     * @param parentBrancheId The ID of the parent branch, if it exists.
     * @param name The name of the branch.
     * @param description A short description of the branch.
     */
    public BranchSummaryDto(Long id, Long parentBrancheId, String name, String description) {
        this.id = id;
        this.parentBrancheId = parentBrancheId;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentBrancheId() {
        return parentBrancheId;
    }
    public void setParentBrancheId(Long parentBrancheId) {
        this.parentBrancheId = parentBrancheId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}