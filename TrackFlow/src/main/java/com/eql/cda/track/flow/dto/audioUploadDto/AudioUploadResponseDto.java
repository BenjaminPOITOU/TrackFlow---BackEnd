package com.eql.cda.track.flow.dto.audioUploadDto;

// Renommez le champ pour plus de clarté
public class AudioUploadResponseDto {

    private String fullUrl;
    private String bpm;
    private Integer durationSeconds;


    public String getFullUrl() {
        return fullUrl;
    }
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getBpm() {
        return bpm;
    }
    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}