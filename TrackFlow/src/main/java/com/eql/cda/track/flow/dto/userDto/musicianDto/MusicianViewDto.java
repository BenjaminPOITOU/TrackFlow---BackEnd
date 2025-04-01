package com.eql.cda.track.flow.dto.userDto.musicianDto;

import com.eql.cda.track.flow.entity.UserRole;

import java.time.LocalDateTime;
import java.util.Date;

public class MusicianViewDto {

    private Long id; // Essentiel pour identifier l'utilisateur
    private String login; // Ou login, selon ce que tu utilises comme identifiant principal
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private String biography;
    private String picture;
    private UserRole userRole;
    private LocalDateTime creationDate;



    // Constructeur (souvent utilisé par le mapper/service pour convertir l'entité User en UserViewDto)
    public MusicianViewDto(Long id, String login, String firstName, String lastName, String mobile, String address, String biography, String picture, UserRole userRole, LocalDateTime creationDate) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.address = address;
        this.biography = biography;
        this.picture = picture;
        this.userRole = userRole;
        this.creationDate = creationDate;
    }

    // Getters (Setters généralement pas nécessaires pour un DTO de lecture)

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

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

    public String getPicture() {
        return picture;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Setters (inclus car pas de Lombok, mais souvent non utilisés en pratique pour la lecture)
    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

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

    public void setPictureURL(String pictureURL) {
        this.picture = pictureURL;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
