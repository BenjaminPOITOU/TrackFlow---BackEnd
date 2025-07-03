package com.eql.cda.track.flow.dto.versionDto;

import java.time.Instant;

/**
 * A Data Transfer Object that provides a compact, read-only representation of a
 * {@link com.eql.cda.track.flow.entity.Version}. It is designed for use in lists and summary views.
 */
public class VersionSummaryDto {

    private Long id;
    private String name;
    private String branchName;
    private Instant createdDate;

    /**
     * Default constructor required for framework instantiation.
     */
    public VersionSummaryDto() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}