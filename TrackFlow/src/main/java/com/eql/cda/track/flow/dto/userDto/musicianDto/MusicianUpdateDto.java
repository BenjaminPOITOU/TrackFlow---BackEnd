package com.eql.cda.track.flow.dto.userDto.musicianDto;

import com.eql.cda.track.flow.entity.ProfileType;
import jakarta.validation.constraints.Size;

/**
 * @file A DTO for updating a Musician's profile information.
 */

/**
 * A Data Transfer Object used to carry update information for a Musician's profile.
 * All fields are optional, allowing for partial updates. This includes the ability
 * for a user to change their profile type.
 */
public class MusicianUpdateDto {

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String mobile;

    @Size(max = 100)
    private String address;

    private String biography;
    private String picture;
    private ProfileType profileType;

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

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
}