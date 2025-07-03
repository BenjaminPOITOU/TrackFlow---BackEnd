package com.eql.cda.track.flow.dto.versionDto;

import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * A Data Transfer Object that carries the information to update an existing {@link com.eql.cda.track.flow.entity.Version}.
 * All fields are optional, allowing for partial updates (PATCH).
 */
public class VersionUpdateDto {

    @Size(min = 1, max = 255, message = "Version name must be between 1 and 255 characters.")
    private String name;

    @Size(max = 10, message = "BPM string representation cannot exceed 10 characters.")
    private String bpm;

    @Size(max = 10, message = "Musical key cannot exceed 10 characters.")
    private String key;

    private Set<String> instruments;

    /**
     * Default constructor required for framework instantiation.
     */
    public VersionUpdateDto() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public Set<String> getInstruments() {
        return instruments;
    }
    public void setInstruments(Set<String> instruments) {
        this.instruments = instruments;
    }
}