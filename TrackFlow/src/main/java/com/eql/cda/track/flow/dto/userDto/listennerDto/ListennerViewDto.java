package com.eql.cda.track.flow.dto.userDto.listennerDto;

import com.eql.cda.track.flow.entity.UserRole;

public class ListennerViewDto {

    private Long id; // Essentiel pour identifier l'utilisateur
    private String login; // Ou login, selon ce que tu utilises comme identifiant principal
    private String firstName;
    private String lastName;
    private UserRole userRole;


    public ListennerViewDto(Long id, String login, String firstName, String lastName, UserRole userRole) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
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

    public UserRole getUserRole() {
        return userRole;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
