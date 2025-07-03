package com.eql.cda.track.flow.dto.compositionDto;


import com.eql.cda.track.flow.dto.EnumDto;

import java.time.Instant;
import java.util.List;

public class CompositionSummaryDto {

    private Long id;
    private String title;
    private Integer totalBranches;
    private Integer totalVersions;
    private Instant lastUpdateDate;
    private EnumDto status;

    public CompositionSummaryDto() {
    }

    public CompositionSummaryDto(Long id, String title, Integer totalBranches, Integer totalVersions, Instant lastUpdateDate, EnumDto status) {
        this.id = id;
        this.title = title;
        this.totalBranches = totalBranches;
        this.totalVersions = totalVersions;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
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

    public EnumDto getStatus() {
        return status;
    }
    public void setStatus(EnumDto status) {
        this.status = status;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Integer getTotalVersions() {
        return totalVersions;
    }
    public void setTotalVersions(Integer totalVersions) {
        this.totalVersions = totalVersions;
    }
}
