package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CompositionViewDto {

    private Long id;
    private Long projectId;
    private String title;
    private String projectTitle;
    private CompositionStatus compositionStatus;
    private Integer totalBranches;
    private Integer totalVersions;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdateDate;
    private List<String> subGenders;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGenderPreDefinedList;
    private String description;
    private String illustration;


    public CompositionViewDto() {
    }
    public CompositionViewDto(Long id, Long projectId, String title, String projectTitle, CompositionStatus compositionStatus, Integer totalBranches, Integer totalVersions, LocalDateTime createdDate, LocalDateTime lastUpdateDate, List<String> subGenders, Set<ProjectMusicalGenderPreDefined> projectMusicalGenderPreDefinedList, String description, String illustration) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.projectTitle = projectTitle;
        this.compositionStatus = compositionStatus;
        this.totalBranches = totalBranches;
        this.totalVersions = totalVersions;
        this.createdDate = createdDate;
        this.lastUpdateDate = lastUpdateDate;
        this.subGenders = subGenders;
        this.projectMusicalGenderPreDefinedList = projectMusicalGenderPreDefinedList;
        this.description = description;
        this.illustration = illustration;
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

    public Integer getTotalVersions() {
        return totalVersions;
    }
    public void setTotalVersions(Integer totalVersions) {
        this.totalVersions = totalVersions;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
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
