package com.eql.cda.track.flow.dto.userDto.musicianDto;

import com.eql.cda.track.flow.entity.ProfileType;
import java.time.Instant;

/**
 * @file A DTO for displaying a Musician's public profile information.
 */

/**
 * A Data Transfer Object that provides a detailed, read-only representation of a Musician.
 * It is designed to be sent to the client to display a musician's profile.
 * It safely exposes non-sensitive information, including the user's specific profile type
 * (e.g., Artist, Producer).
 */
public class MusicianViewDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private String picture;
    private ProfileType profileType;
    private Instant creationDate;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ProfileType getProfileType() {
        return profileType;
    }
    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public Instant getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}