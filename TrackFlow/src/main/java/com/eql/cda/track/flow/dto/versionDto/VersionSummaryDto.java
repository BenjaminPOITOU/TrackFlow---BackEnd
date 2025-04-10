package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.entity.Branch;

import java.time.Instant;

public class VersionSummaryDto {

    private Long versionId;
    private String versionName;
    private String branchName;
   // private String author;
    private Instant createdDate;


    public VersionSummaryDto() {
    }

    public VersionSummaryDto(Long versionId, String versionName, String branchName, Instant createdDate) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.branchName = branchName;
        //this.author = author;
        this.createdDate = createdDate;
    }

    public Long getVersionId() {
        return versionId;
    }
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    //public String getAuthor() {
     //   return author;
    //}
    //public void setAuthor(String author) {
    //    this.author = author;
   // }

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}