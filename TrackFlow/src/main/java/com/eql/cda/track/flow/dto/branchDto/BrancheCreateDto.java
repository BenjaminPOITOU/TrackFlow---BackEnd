package com.eql.cda.track.flow.dto.branchDto;

public class BrancheCreateDto {

    private Long compositionId;
    private Long parentVersionId;
    private String name;
    private String description;

    public BrancheCreateDto() {
    }
    public BrancheCreateDto(Long compositionId, Long parentVersionId, String name, String description) {
        this.compositionId = compositionId;
        this.parentVersionId = parentVersionId;
        this.name = name;
        this.description = description;
    }


    public Long getCompositionId() { return compositionId; }
    public Long getParentVersionId() { return parentVersionId; }
    public String getName() { return name; }
    public String getDescription() { return description; }


    public void setCompositionId(Long compositionId) { this.compositionId = compositionId; }
    public void setParentVersionId(Long parentVersionId) { this.parentVersionId = parentVersionId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
