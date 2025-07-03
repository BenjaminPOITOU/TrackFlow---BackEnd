package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * A Data Transfer Object that provides a detailed, read-only representation of a
 * {@link com.eql.cda.track.flow.entity.Version}. It is designed to be sent to the client
 * for display on a version's detail page, including its instruments and annotations.
 */
public class VersionViewDto {

    private Long id;
    private String name;
    private String author;
    private String audioFileUrl;
    private Integer durationSeconds;
    private String bpm;
    private String key;
    private Instant createdDate;
    private Long branchId;
    private String branchDescription;
    private String branchName;
    private Long parentVersionId;
    private Set<VersionInstrumentPreDefined> instruments;
    private List<AnnotationViewDto> annotations;

    /**
     * Default constructor required for framework instantiation.
     */
    public VersionViewDto() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
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

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchDescription() {
        return branchDescription;
    }
    public void setBranchDescription(String branchDescription) {
        this.branchDescription = branchDescription;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Long getParentVersionId() {
        return parentVersionId;
    }
    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public Set<VersionInstrumentPreDefined> getInstruments() {
        return instruments;
    }
    public void setInstruments(Set<VersionInstrumentPreDefined> instruments) {
        this.instruments = instruments;
    }

    public List<AnnotationViewDto> getAnnotations() {
        return annotations;
    }
    public void setAnnotations(List<AnnotationViewDto> annotations) {
        this.annotations = annotations;
    }
}