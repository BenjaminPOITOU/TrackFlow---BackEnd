package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.ProjectStatus;
import java.time.Instant;
import java.util.List;

/**
 * DTO for a summary view of a project, typically used in lists or dashboards.
 */
public class ProjectSummaryDto {

    private Long id;
    private String title;
    private ProjectStatus projectStatus;
    private List<String> projectMusicalGendersPreDefined;
    private Instant createdDate;

    public ProjectSummaryDto() {
    }

    public ProjectSummaryDto(Long id, String title, ProjectStatus projectStatus, List<String> projectMusicalGendersPreDefined, Instant createdDate) {
        this.id = id;
        this.title = title;
        this.projectStatus = projectStatus;
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
        this.createdDate = createdDate;
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

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<String> getProjectMusicalGendersPreDefined() {
        return projectMusicalGendersPreDefined;
    }

    public void setProjectMusicalGendersPreDefined(List<String> projectMusicalGendersPreDefined) {
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}