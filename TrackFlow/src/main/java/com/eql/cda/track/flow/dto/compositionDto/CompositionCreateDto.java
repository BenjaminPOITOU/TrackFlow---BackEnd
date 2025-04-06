package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.validation.Constants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@NotNull(message = "Compositon creation cannot be null")
public class CompositionCreateDto {

    @Size(max = Constants.COMPOSITION_TITLE_MAX_LENGTH, message = Constants.COMPOSITION_TITLE_MAX_LENGTH_MSG)
    @Column(length = Constants.COMPOSITION_TITLE_MAX_LENGTH)
    @NotNull(message = "Composition title cannot be null")
    @NotBlank(message = "Composition title cannot be blank")
    private String title;


    @NotNull(message = "Composition Status cannot be null")
    private CompositionStatus compositionStatus;

    @NotNull(message = "Composition subgender cannot be null")
    private List<String> subGender;

    private String description;
    private String illustration;

    public CompositionCreateDto() {
    }
    public CompositionCreateDto(String title, CompositionStatus compositionStatus, List<String> subGender,  String description, String illustration) {
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
