package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class VersionCreateDto {
    /**
     * A Data Transfer Object that carries all the necessary information to create a new {@link com.eql.cda.track.flow.entity.Version}.
     * This is a complex DTO used as the request body for version creation endpoints.
     */
    @NotNull(message = "Branch ID is mandatory.")
    @Positive(message = "Branch ID must be a positive number.")
    private Long branchId;

    @Positive(message = "Parent Version ID must be positive if provided.")
    private Long parentVersionId;

    @NotBlank(message = "Version name cannot be blank.")
    private String versionName;

    @NotBlank(message = "Audio file URL from storage is mandatory.")
    private String audioFileUrl;

    @NotNull(message = "Duration in seconds is mandatory.")
    @PositiveOrZero(message = "Duration must be zero or positive.")
    private Integer durationSeconds;

    @Size(max = 10, message = "BPM string representation cannot exceed 10 characters.")
    private String bpm;

    @Size(max = 10, message = "Musical key cannot exceed 10 characters.")
    private String key;

    @NotNull(message = "Instrument list cannot be null (can be empty).")
    private Set<String> versionInstrumentPreDefinedList;

    @NotNull(message = "List of annotation IDs to resolve cannot be null (can be empty).")
    private List<Long> annotationIdsToResolve;

    @Valid
    @NotNull(message = "List of annotations to create cannot be null (can be empty).")
    private List<AnnotationCreateDto> annotationsToCreate;

    private Map<String, String> metadata;

    public VersionCreateDto() {
    }

    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getParentVersionId() {
        return parentVersionId;
    }
    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }
    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getBpm() {
        return bpm;
    }
    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public Set<String> getVersionInstrumentPreDefinedList() {
        return versionInstrumentPreDefinedList;
    }
    public void setVersionInstrumentPreDefinedList(Set<String> versionInstrumentPreDefinedList) {
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
    }

    public List<Long> getAnnotationIdsToResolve() {
        return annotationIdsToResolve;
    }
    public void setAnnotationIdsToResolve(List<Long> annotationIdsToResolve) {
        this.annotationIdsToResolve = annotationIdsToResolve;
    }

    public List<AnnotationCreateDto> getAnnotationsToCreate() {
        return annotationsToCreate;
    }
    public void setAnnotationsToCreate(List<AnnotationCreateDto> annotationsToCreate) {
        this.annotationsToCreate = annotationsToCreate;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}