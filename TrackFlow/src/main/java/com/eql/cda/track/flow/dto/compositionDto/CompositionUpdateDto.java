package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.CompositionStatus;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * A Data Transfer Object used to carry update information for a
 * {@link Composition}.
 * All fields in this DTO are optional, allowing for partial updates of a composition.
 */
public class CompositionUpdateDto {

    @Size(max = 50, message = "Title must be less than 50 characters.")
    private String title;

    private CompositionStatus compositionStatus;
    private List<String> subGender;
    private String description;
    private String illustration;

    /**
     * Default constructor required for framework instantiation.
     */
    public CompositionUpdateDto() {
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