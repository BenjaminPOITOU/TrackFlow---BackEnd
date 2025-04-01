package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;

import java.util.Set;

public class VersionCreateDto {


    private Long branchId;
    private String title;
    private String description;
    private Set<String> instrumentsChosenByUser; // Idem
    private Set<VersionInstrumentPreDefined> versionInstrumentsPreDefinedSet; // Idem


    public VersionCreateDto() {
    }
    public VersionCreateDto(Long branchId, String title, String description, Set<String> instrumentsChosenByUser, Set<VersionInstrumentPreDefined> versionInstrumentsPreDefinedSet) {
        this.branchId = branchId;
        this.title = title;
        this.description = description;
        this.instrumentsChosenByUser = instrumentsChosenByUser;
        this.versionInstrumentsPreDefinedSet = versionInstrumentsPreDefinedSet;
    }

    public Long getBranchId() {
        return branchId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Set<String> getInstrumentsChosenByUser() {
        return instrumentsChosenByUser;
    }
    public Set<VersionInstrumentPreDefined> getVersionInstrumentsPreDefinedSet() {
        return versionInstrumentsPreDefinedSet;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
