package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.CompositionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for creating a new composition.
 */
public class CompositionCreateDto {

    @NotBlank(message = "Composition title cannot be blank.")
    @Size(max = 150, message = "Title must be less than 150 characters.")
    private String title;

    @NotNull(message = "Composition status cannot be null.")
    private CompositionStatus compositionStatus;

    private List<String> subGender;
    private String description;
    private String illustration;

    public CompositionCreateDto() {
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