package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectType;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ProjectViewDto {

    private Long id;
    private String title;
    private String description;
    private String illustration;
    private ProjectStatus projectStatus;
    private ProjectType projectType;
    private ProjectCommercialStatus projectCommercialStatus;
    private Set<ProjectPurpose> projectPurposes;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined;
    private Instant creationDate;
    private Instant updateDate;
    private Integer compositionsTotal;



    public ProjectViewDto() {
    }

    public ProjectViewDto(Long id, String title, String description, String illustration, ProjectStatus projectStatus, ProjectType projectType, ProjectCommercialStatus projectCommercialStatus, Set<ProjectPurpose> projectPurposes, Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined, Instant creationDate, Instant updateDate, Integer compositionsTotal) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.illustration = illustration;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectCommercialStatus = projectCommercialStatus;
        this.projectPurposes = projectPurposes;
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.compositionsTotal = compositionsTotal;
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

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ProjectCommercialStatus getProjectCommercialStatus() {
        return projectCommercialStatus;
    }

    public void setProjectCommercialStatus(ProjectCommercialStatus projectCommercialStatus) {
        this.projectCommercialStatus = projectCommercialStatus;
    }

    public Set<ProjectPurpose> getProjectPurposes() {
        return projectPurposes;
    }

    public void setProjectPurposes(Set<ProjectPurpose> projectPurposes) {
        this.projectPurposes = projectPurposes;
    }

    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGendersPreDefined() {
        return projectMusicalGendersPreDefined;
    }

    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) {
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getCompositionsTotal() {
        return compositionsTotal;
    }

    public void setCompositionsTotal(Integer compositionsTotal) {
        this.compositionsTotal = compositionsTotal;
    }
}
