package com.eql.cda.track.flow.dto.branchDto;

public class GraphBranchDto {

    private Long branchId;
    private Long parentBranchId;
    private String branchName;


    public GraphBranchDto() {
    }
    public GraphBranchDto(Long branchId, Long parentBranchId, String branchName) {
        this.branchId = branchId;
        this.parentBranchId = parentBranchId;
        this.branchName = branchName;
    }

    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getParentBranchId() {
        return parentBranchId;
    }
    public void setParentBranchId(Long parentBranchId) {
        this.parentBranchId = parentBranchId;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
