package com.eql.cda.track.flow.dto.versionDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class VersionResponseDto {

    private Long id;
    private String name;
    private String author;
    private String bpm;
    private String key;
    private Integer durationSeconds;
    private String branchDescription;
    private String audioFileUrl;
    private Map<String, String> metadata;
    private LocalDateTime createdDate;
    private Long branchId;
    private String branchName;
    // Simplifié : juste les noms des instruments
    private Set<String> instruments;

    public VersionResponseDto() {
    }
    public VersionResponseDto(Long id, String name, String author, String bpm, String key, Integer durationSeconds, String branchDescription, String audioFileUrl, Map<String, String> metadata, LocalDateTime createdDate, Long branchId, String branchName, Set<String> instruments) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.bpm = bpm;
        this.key = key;
        this.durationSeconds = durationSeconds;
        this.branchDescription = branchDescription;
        this.audioFileUrl = audioFileUrl;
        this.metadata = metadata;
        this.createdDate = createdDate;
        this.branchId = branchId;
        this.branchName = branchName;
        this.instruments = instruments;
    }


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAuthor() {
        return author;
    }
    public String getBpm() {
        return bpm;
    }
    public String getKey() {
        return key;
    }
    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public String getBranchDescription() {
        return branchDescription;
    }
    public String getAudioFileUrl() {
        return audioFileUrl;
    }
    public Map<String, String> getMetadata() {
        return metadata;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public Long getBranchId() {
        return branchId;
    }
    public String getBranchName() {
        return branchName;
    }
    public Set<String> getInstruments() {
        return instruments;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setBpm(String bpm) {
        this.bpm = bpm;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
    public void setBranchDescription(String branchDescription) {
        this.branchDescription = branchDescription;
    }
    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public void setInstruments(Set<String> instruments) {
        this.instruments = instruments;
    }
}
