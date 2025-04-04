package com.eql.cda.track.flow.dto.versionDto;

public class AudioUploadResponseDto {

    private String url;
    private Integer bpm;
    private Integer durationSeconds;


    public AudioUploadResponseDto() {
    }
    public AudioUploadResponseDto(String url, Integer bpm, Integer durationSeconds) {
        this.url = url;
        this.bpm = bpm;
        this.durationSeconds = durationSeconds;
    }


    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
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
