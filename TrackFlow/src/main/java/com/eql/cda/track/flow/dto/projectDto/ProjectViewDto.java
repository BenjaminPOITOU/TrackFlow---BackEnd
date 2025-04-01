package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderAdded;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectTag;
import com.eql.cda.track.flow.entity.ProjectType;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectViewDto {

    private String title;
    private String description; // Optionnel
    private String illustration;
    private ProjectStatus projectStatus;
    private ProjectType projectType;
    private ProjectCommercialStatus projectCommercialStatus;
    private Set<ProjectPurpose> projectPurposes;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined;
    private Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet;
    private Set<ProjectTag> projectTagSet;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Integer compositionsTotal;



    public ProjectViewDto() {
    }


    public ProjectViewDto(String title, String description, String illustration, ProjectStatus projectStatus, ProjectType projectType, ProjectCommercialStatus projectCommercialStatus, Set<ProjectPurpose> projectPurposes, Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined, Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet, Set<ProjectTag> projectTagSet, LocalDateTime creationDate, LocalDateTime updateDate, Integer compositionsTotal) {
        this.title = title;
        this.description = description;
        this.illustration = illustration;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectCommercialStatus = projectCommercialStatus;
        this.projectPurposes = projectPurposes;
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
        this.projectMusicalGenderAddedSet = projectMusicalGenderAddedSet;
        this.projectTagSet = projectTagSet;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.compositionsTotal = compositionsTotal;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIllustrationURL() { return illustration; }
    public ProjectStatus getProjectStatus() { return projectStatus; }
    public ProjectType getProjectType() { return projectType; }
    public ProjectCommercialStatus getProjectCommercialStatus() { return projectCommercialStatus; }
    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGendersPreDefined() {
        return projectMusicalGendersPreDefined;
    }
    public Set<ProjectPurpose> getProjectPurposes() {
        return projectPurposes;
    }
    public Set<ProjectMusicalGenderAdded> getProjectMusicalGenderAddedSet() {
        return projectMusicalGenderAddedSet;
    }
    public Set<ProjectTag> getProjectTagSet() {
        return projectTagSet;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
    public String getIllustration() {
        return illustration;
    }
    public Integer getCompositionsTotal() {
        return compositionsTotal;
    }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setIllustration(String illustration) { this.illustration = illustration; }
    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }
    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }
    public void setProjectCommercialStatus(ProjectCommercialStatus projectCommercialStatus) {
        this.projectCommercialStatus = projectCommercialStatus;
    }
    public void setProjectPurposes(Set<ProjectPurpose> projectPurposes) {
        this.projectPurposes = projectPurposes;
    }
    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) {
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
    }
    public void setProjectMusicalGenderAddedSet(Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet) {
        this.projectMusicalGenderAddedSet = projectMusicalGenderAddedSet;
    }
    public void setProjectTagSet(Set<ProjectTag> projectTagSet) {
        this.projectTagSet = projectTagSet;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
    public void setCompositionsTotal(Integer compositionsTotal) {
        this.compositionsTotal = compositionsTotal;
    }
}
