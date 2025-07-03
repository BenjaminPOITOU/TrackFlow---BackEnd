package com.eql.cda.track.flow.dto.branchDto;

/**
 * A Data Transfer Object that carries the information to update an existing {@link com.eql.cda.track.flow.entity.Branch}.
 * All fields are optional, allowing for partial updates (PATCH).
 */
public class BranchUpdateDto {

    private String name;
    private String description;
    private Long parentBranchId;

    /**
     * Default constructor required for framework instantiation (e.g., JSON deserialization).
     */
    public BranchUpdateDto() {
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

    public Long getParentBranchId() {
        return parentBranchId;
    }
    public void setParentBranchId(Long parentBranchId) {
        this.parentBranchId = parentBranchId;
    }
}