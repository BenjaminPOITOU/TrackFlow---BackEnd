package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.CompositionStatus;

public class CompositionCreateDto {

    private Long projectId;
    private String title;
    private String description;
    private String illustration;
    private CompositionStatus compositionStatus;
    private String subGender;


    public CompositionCreateDto() {
    }

    public CompositionCreateDto(Long projectId, String title, String description, String illustration, CompositionStatus compositionStatus, String subGender) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.illustration = illustration;
        this.compositionStatus = compositionStatus;
        this.subGender = subGender;
    }

    public Long getProjectId() {
        return projectId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getIllustration() {
        return illustration;
    }
    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }
    public String getSubGender() {
        return subGender;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }
    public void setSubGender(String subGender) {
        this.subGender = subGender;
    }
}
