package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;

import java.util.List;

public class NewVersionModalDto {

    private Long currentVersionId; // L'ID de la version sur laquelle l'utilisateur a cliqué
    private Long currentBranchId;  // L'ID de la branche de la version actuelle
    private String currentBranchName; // Le nom de la branche actuelle (pour affichage)
    private List<BranchSummaryDto> availableBranches; // Toutes les branches de la composition
    private String potentialNextVersionName; // Nom calculé pour la prochaine version sur la branche actuelle
    private List<VersionInstrumentPreDefined> previousVersionInstruments; // Instruments de la version actuelle

    // TODO: Ajouter plus tard:
    // private List<AnnotationSummaryDto> previousVersionAnnotations;


    public NewVersionModalDto() {
    }

    public NewVersionModalDto(Long currentVersionId, Long currentBranchId, String currentBranchName, List<BranchSummaryDto> availableBranches, String potentialNextVersionName, List<VersionInstrumentPreDefined> previousVersionInstruments) {
        this.currentVersionId = currentVersionId;
        this.currentBranchId = currentBranchId;
        this.currentBranchName = currentBranchName;
        this.availableBranches = availableBranches;
        this.potentialNextVersionName = potentialNextVersionName;
        this.previousVersionInstruments = previousVersionInstruments;
    }

    public Long getCurrentVersionId() {
        return currentVersionId;
    }

    public void setCurrentVersionId(Long currentVersionId) {
        this.currentVersionId = currentVersionId;
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
}
