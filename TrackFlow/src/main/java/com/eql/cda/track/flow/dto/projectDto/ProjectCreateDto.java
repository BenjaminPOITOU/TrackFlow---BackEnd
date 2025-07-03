package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * DTO for creating a new project. Contains all required fields for creation.
 */
public class ProjectCreateDto {

    @NotBlank(message = "Project title is mandatory.")
    @Size(max = 150, message = "Project title cannot exceed 150 characters.")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    private String illustration;

    @NotNull(message = "Project status is mandatory.")
    private ProjectStatus projectStatus;

    @NotNull(message = "Project type is mandatory.")
    private ProjectType projectType;

    @NotNull(message = "Commercial status is mandatory.")
    private ProjectCommercialStatus projectCommercialStatus;

    private Set<ProjectPurpose> projectPurposes;
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined;

    public ProjectCreateDto() {
    }

    // Getters and Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIllustration() { return illustration; }
    public void setIllustration(String illustration) { this.illustration = illustration; }
    public ProjectStatus getProjectStatus() { return projectStatus; }
    public void setProjectStatus(ProjectStatus projectStatus) { this.projectStatus = projectStatus; }
    public ProjectType getProjectType() { return projectType; }
    public void setProjectType(ProjectType projectType) { this.projectType = projectType; }
    public ProjectCommercialStatus getProjectCommercialStatus() { return projectCommercialStatus; }
    public void setProjectCommercialStatus(ProjectCommercialStatus projectCommercialStatus) { this.projectCommercialStatus = projectCommercialStatus; }
    public Set<ProjectPurpose> getProjectPurposes() { return projectPurposes; }
    public void setProjectPurposes(Set<ProjectPurpose> projectPurposes) { this.projectPurposes = projectPurposes; }
    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGendersPreDefined() { return projectMusicalGendersPreDefined; }
    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) { this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined; }
}