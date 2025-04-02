package com.eql.cda.track.flow.dto.compositionDto;


import java.time.LocalDateTime;
import java.util.List;

public class CompositionSummaryDto {

    private Long id;
    private String title;
    private Integer totalBranches;
    private Integer totalVersions;
    private List<String> subGenders;
    private LocalDateTime lastUpdateDate;

    public CompositionSummaryDto() {
    }

    public CompositionSummaryDto(Long id, String title, Integer totalBranches, Integer totalVersions, List<String> subGenders, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.title = title;
        this.totalBranches = totalBranches;
        this.totalVersions = totalVersions;
        this.subGenders = subGenders;
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalBranches() {
        return totalBranches;
    }
    public void setTotalBranches(Integer totalBranches) {
        this.totalBranches = totalBranches;
    }

    public List<String> getSubGenders() {
        return subGenders;
    }
    public void setSubGenders(List<String> subGenders) {
        this.subGenders = subGenders;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Integer getTotalVersions() {
        return totalVersions;
    }
    public void setTotalVersions(Integer totalVersions) {
        this.totalVersions = totalVersions;
    }
}
