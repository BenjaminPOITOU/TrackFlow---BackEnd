package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.validation.Constants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CompositionUpdateDto {

    @Size(max = Constants.COMPOSITION_TITLE_MAX_LENGTH, message = Constants.COMPOSITION_TITLE_MAX_LENGTH_MSG)
    @Column(length = Constants.COMPOSITION_TITLE_MAX_LENGTH)
    private String title;

    @Size(max = Constants.COMPOSITION_DESC_MAX_LENGTH, message = Constants.COMPOSITION_DESC_MAX_LENGTH_MSG)
    @Column(length = Constants.COMPOSITION_DESC_MAX_LENGTH)
    private CompositionStatus compositionStatus;

    private List<String> subGender;
    private String description;
    private String illustration;

    public CompositionUpdateDto() {
    }
    public CompositionUpdateDto(String title, CompositionStatus compositionStatus, List<String> subGender, String description, String illustration) {
        this.title = title;
        this.compositionStatus = compositionStatus;
        this.subGender = subGender;
        this.description = description;
        this.illustration = illustration;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }
    public void setCompositionStatus(CompositionStatus compositionStatus) {
        this.compositionStatus = compositionStatus;
    }

    public List<String> getSubGender() {
        return subGender;
    }
    public void setSubGender(List<String> subGender) {
        this.subGender = subGender;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIllustration() {
        return illustration;
    }
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }
}
