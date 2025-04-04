package com.eql.cda.track.flow.dto;


public class AudioMetadataDto {

    private Integer bpm;
    private Integer durationSeconds;

    public AudioMetadataDto(Integer bpm, Integer durationSeconds) {
        this.bpm = bpm;
        this.durationSeconds = durationSeconds;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}