package com.eql.cda.track.flow.dto.projectDto;

import com.eql.cda.track.flow.entity.ProjectStatus;

import java.time.Instant;
import java.util.List;

public class ProjectSummaryDto {

    private Long id; // <-- Important pour le routing frontend !
    private String title;
    private ProjectStatus projectStatus;
    private List<String> musicalGenres;
    private Instant lastUpdateDate;

    public ProjectSummaryDto() {
    }
    public ProjectSummaryDto(Long id, String title, ProjectStatus projectStatus, List<String> musicalGenres, Instant lastUpdateDate) {
        this.id = id;
        this.title = title;
        this.projectStatus = projectStatus;
        this.musicalGenres = musicalGenres;
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

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }
    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<String> getMusicalGenres() {
        return musicalGenres;
    }
    public void setMusicalGenres(List<String> musicalGenres) {
        this.musicalGenres = musicalGenres;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
