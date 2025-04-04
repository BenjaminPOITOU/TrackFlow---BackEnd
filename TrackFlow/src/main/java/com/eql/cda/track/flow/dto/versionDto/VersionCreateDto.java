package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.Map;

public class VersionCreateDto {


    @Positive(message = "Branch ID must be a positive number.")
    private Long branchId;

    @Positive(message = "Parent Branch ID must be a positive number.")
    private Long parentBranchId;

    @Size(max = 100, message = "New branch name cannot exceed 100 characters.")
    private String newBranchName;

    @Size(max = 500, message = "Description cannot exceed 500 characters.")
    private String description;

    @NotBlank(message = "Audio file URL is mandatory.")
    @URL(message = "Audio file URL must be a valid URL.")
    @Size(max = 2048, message = "Audio file URL is too long (max 2048 chars).")
    private String audioFileUrl;

    @Positive(message = "Parent Version ID must be a positive number.")
    private Long parentVersionId;

    @PositiveOrZero(message = "Duration must be zero or positive.")
    private Integer durationSeconds;

    @Size(max = 10, message = "BPM string representation cannot exceed 10 characters.")
    private String bpm;

    @Size(max = 10, message = "Musical key cannot exceed 10 characters.")
    private String key;

    private List<VersionInstrumentPreDefined> versionInstrumentPreDefinedList;

    @Valid // Validate elements if Annotation entity has constraints (Consider AnnotationCreateDto)
    private List<AnnotationCreateDto> annotations; // << Consider replacing with List<AnnotationCreateDto>

    private Map<String, String> metadata;


    public VersionCreateDto() {
    }

    public VersionCreateDto(Long branchId, Long parentBranchId, String newBranchName, String description, String audioFileUrl, Long parentVersionId, Integer durationSeconds, String bpm, String key, List<VersionInstrumentPreDefined> versionInstrumentPreDefinedList, List<AnnotationCreateDto> annotations, Map<String, String> metadata) {
        this.branchId = branchId;
        this.parentBranchId = parentBranchId;
        this.newBranchName = newBranchName;
        this.description = description;
        this.audioFileUrl = audioFileUrl;
        this.parentVersionId = parentVersionId;
        this.durationSeconds = durationSeconds;
        this.bpm = bpm;
        this.key = key;
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
        this.annotations = annotations;
        this.metadata = metadata;
    }

    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getParentBranchId() {
        return parentBranchId;
    }

    public void setParentBranchId(Long parentBranchId) {
        this.parentBranchId = parentBranchId;
    }

    public String getNewBranchName() {
        return newBranchName;
    }
    public void setNewBranchName(String newBranchName) {
        this.newBranchName = newBranchName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }
    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }


    public Long getParentVersionId() {
        return parentVersionId;
    }
    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public List<AnnotationCreateDto> getAnnotations() {
        return annotations;
    }
    public void setAnnotations(List<AnnotationCreateDto> annotations) {
        this.annotations = annotations;
    }

    public List<VersionInstrumentPreDefined> getVersionInstrumentPreDefinedList() {
        return versionInstrumentPreDefinedList;
    }
    public void setVersionInstrumentPreDefinedList(List<VersionInstrumentPreDefined> versionInstrumentPreDefinedList) {
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
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

    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}
