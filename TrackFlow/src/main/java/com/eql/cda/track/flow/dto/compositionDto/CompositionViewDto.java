package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * A Data Transfer Object that provides a detailed, read-only representation of a
 * {@link Composition}.
 * It is designed to be sent to the client for display on a composition's detail page.
 */
public class CompositionViewDto {

    private Long id;
    private Long projectId;
    private String title;
    private String projectTitle;
    private CompositionStatus compositionStatus;
    private Integer totalBranches;
    private List<BranchSummaryDto> branches;
    private Integer totalVersions;
    private Instant createdDate;
    private Instant lastUpdateDate;
    private List<String> subGenders;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGenderPreDefinedList;
    private String description;
    private String illustration;

    /**
     * Default constructor required for framework instantiation.
     */
    public CompositionViewDto() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectTitle() {
        return projectTitle;
    }
    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }
    public void setCompositionStatus(CompositionStatus compositionStatus) {
        this.compositionStatus = compositionStatus;
    }

    public Integer getTotalBranches() {
        return totalBranches;
    }
    public void setTotalBranches(Integer totalBranches) {
        this.totalBranches = totalBranches;
    }

    public List<BranchSummaryDto> getBranches() {
        return branches;
    }
    public void setBranches(List<BranchSummaryDto> branches) {
        this.branches = branches;
    }

    public Integer getTotalVersions() {
        return totalVersions;
    }
    public void setTotalVersions(Integer totalVersions) {
        this.totalVersions = totalVersions;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<String> getSubGenders() {
        return subGenders;
    }
    public void setSubGenders(List<String> subGenders) {
        this.subGenders = subGenders;
    }

    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGenderPreDefinedList() {
        return projectMusicalGenderPreDefinedList;
    }
    public void setProjectMusicalGenderPreDefinedList(Set<ProjectMusicalGenderPreDefined> projectMusicalGenderPreDefinedList) {
        this.projectMusicalGenderPreDefinedList = projectMusicalGenderPreDefinedList;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIllustration() {
        return illustration;
    }
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }
}