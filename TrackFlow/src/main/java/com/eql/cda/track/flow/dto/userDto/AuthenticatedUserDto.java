package com.eql.cda.track.flow.dto.userDto;

import com.eql.cda.track.flow.entity.ProfileType;

public class AuthenticatedUserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private ProfileType userRole;
    private String token;

    // Constructeur
    public AuthenticatedUserDto(Long id, String email, String firstName, String lastName, ProfileType userRole, String token) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
        this.token = token; // Initialisation du token
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ProfileType getUserRole() {
        return userRole;
    }

    public String getToken() {
        return token; // Getter pour le token
    }

    // Setters (inclus car pas de Lombok)
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserRole(ProfileType userRole) {
        this.userRole = userRole;
    }

    public void setToken(String token) {
        this.token = token; // Setter pour le token
    }
}
