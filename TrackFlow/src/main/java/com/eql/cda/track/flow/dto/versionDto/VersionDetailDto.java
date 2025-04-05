package com.eql.cda.track.flow.dto.versionDto;



import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;

import java.util.List;

public class VersionDetailDto {

    private Long versionId;
    private String versionName;
    private String branchName;
    private String description;
    private String audioFileUrl;
    private String bpm;
    private String key;
    private Integer durationSeconds;
    private List<VersionInstrumentDto> versionInstruments;
    private List<AnnotationResponseDto> annotationResponseDtoList;


    public VersionDetailDto() {
    }

    public VersionDetailDto(Long versionId, String versionName, String branchName, String description, String audioFileUrl, String bpm, String key, Integer durationSeconds, List<VersionInstrumentDto> versionInstruments, List<AnnotationResponseDto> annotationResponseDtoList) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.branchName = branchName;
        this.description = description;
        this.audioFileUrl = audioFileUrl;
        this.bpm = bpm;
        this.key = key;
        this.durationSeconds = durationSeconds;
        this.versionInstruments = versionInstruments;
        this.annotationResponseDtoList = annotationResponseDtoList;
    }

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    public Long getVersionId() {
        return versionId;
    }
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public List<VersionInstrumentDto> getVersionInstruments() {
        return versionInstruments;
    }
    public void setVersionInstruments(List<VersionInstrumentDto> versionInstruments) {
        this.versionInstruments = versionInstruments;
    }

    public List<AnnotationResponseDto> getAnnotationResponseDtoList() {
        return annotationResponseDtoList;
    }
    public void setAnnotationResponseDtoList(List<AnnotationResponseDto> annotationResponseDtoList) {
        this.annotationResponseDtoList = annotationResponseDtoList;
    }
}
