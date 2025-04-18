package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectType;
import com.eql.cda.track.flow.validation.Constants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ProjectCreateDto {

    private Long id;

    @Size(max = Constants.PROJECT_TITLE_MAX_LENGTH, message = Constants.PROJECT_TITLE_MAX_LENGTH_MSG)
    @Column(length = Constants.PROJECT_TITLE_MAX_LENGTH)
    private String title;

    @Size(max = Constants.PROJECT_DESC_MAX_LENGTH, message = Constants.PROJECT_DESC_MAX_LENGTH_MSG)
    @Column(length = Constants.PROJECT_DESC_MAX_LENGTH)
    private String description;


    private String illustration;
    private ProjectStatus projectStatus;
    private ProjectType projectType;
    private ProjectCommercialStatus projectCommercialStatus;
    private Set<ProjectPurpose> projectPurposes;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined;
    private Instant creationDate;


    public ProjectCreateDto() {
    }


    public ProjectCreateDto(Long id, String title, String description, String illustration, ProjectStatus projectStatus, ProjectType projectType, ProjectCommercialStatus projectCommercialStatus, Set<ProjectPurpose> projectPurposes, Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined, Instant creationDate) {
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
}
