package com.eql.cda.track.flow.dto.userDto.musicianDto;

import com.eql.cda.track.flow.entity.UserRole;

public class MusicianProfileUpdateDto {

    // --- Champs Modifiables du Profil ---


    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private String biography;
    private String picture;
    private UserRole userRole; // URL de la nouvelle photo de profil

    public MusicianProfileUpdateDto() {
    }

    public MusicianProfileUpdateDto(String firstName, String lastName, String mobile, String address, String biography, String picture, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.address = address;
        this.biography = biography;
        this.picture = picture;
        this.userRole = userRole;
    }

    // Peut-être @NotEmpty si fourni, mais le champ lui-même peut être optionnel dans la requête
    public String getFirstName() {
        return firstName;
    }

    // Peut-être @NotEmpty si fourni
    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getBiography() {
        return biography;
    }

    // Peut-être @URL si fourni
    public String getPicture() {
        return picture;
    }

    public UserRole getUserRole() {
        return userRole;
    }
    // --- Setters ---


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
