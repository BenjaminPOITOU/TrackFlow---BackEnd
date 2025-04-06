package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;

import java.util.List;

public class NewVersionModalDto {

    private Long parentVersionId;
    private Long currentBranchId;  // L'ID de la branche de la version actuelle
    private String currentBranchName; // Le nom de la branche actuelle (pour affichage)
    private List<BranchSummaryDto> availableBranches; // Toutes les branches de la composition
    private String potentialNextVersionName; // Nom calculé pour la prochaine version sur la branche actuelle
    private List<VersionInstrumentPreDefined> previousVersionInstruments; // Instruments de la version actuelle
    private boolean canCreateNewBranch;

    private List<AnnotationResponseDto> previousVersionAnnotations;


    public NewVersionModalDto() {
    }

    public NewVersionModalDto(Long parentVersionId, Long currentBranchId, String currentBranchName, List<BranchSummaryDto> availableBranches, String potentialNextVersionName, List<VersionInstrumentPreDefined> previousVersionInstruments, boolean canCreateNewBranch, List<AnnotationResponseDto> previousVersionAnnotations) {
        this.parentVersionId = parentVersionId;
        this.currentBranchId = currentBranchId;
        this.currentBranchName = currentBranchName;
        this.availableBranches = availableBranches;
        this.potentialNextVersionName = potentialNextVersionName;
        this.previousVersionInstruments = previousVersionInstruments;
        this.canCreateNewBranch = canCreateNewBranch;
        this.previousVersionAnnotations = previousVersionAnnotations;
    }

    public Long getParentVersionId() {
        return parentVersionId;
    }

    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public Long getCurrentBranchId() {
        return currentBranchId;
    }

    public void setCurrentBranchId(Long currentBranchId) {
        this.currentBranchId = currentBranchId;
    }

    public String getCurrentBranchName() {
        return currentBranchName;
    }

    public void setCurrentBranchName(String currentBranchName) {
        this.currentBranchName = currentBranchName;
    }

    public List<BranchSummaryDto> getAvailableBranches() {
        return availableBranches;
    }

    public void setAvailableBranches(List<BranchSummaryDto> availableBranches) {
        this.availableBranches = availableBranches;
    }

    public String getPotentialNextVersionName() {
        return potentialNextVersionName;
    }

    public void setPotentialNextVersionName(String potentialNextVersionName) {
        this.potentialNextVersionName = potentialNextVersionName;
    }

    public List<VersionInstrumentPreDefined> getPreviousVersionInstruments() {
        return previousVersionInstruments;
    }

    public void setPreviousVersionInstruments(List<VersionInstrumentPreDefined> previousVersionInstruments) {
        this.previousVersionInstruments = previousVersionInstruments;
    }

    public List<AnnotationResponseDto> getPreviousVersionAnnotations() {
        return previousVersionAnnotations;
    }

    public void setPreviousVersionAnnotations(List<AnnotationResponseDto> previousVersionAnnotations) {
        this.previousVersionAnnotations = previousVersionAnnotations;
    }

    public boolean isCanCreateNewBranch() {
        return canCreateNewBranch;
    }

    public void setCanCreateNewBranch(boolean canCreateNewBranch) {
        this.canCreateNewBranch = canCreateNewBranch;
    }
}
