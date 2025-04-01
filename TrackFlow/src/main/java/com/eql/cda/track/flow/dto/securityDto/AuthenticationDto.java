package com.eql.cda.track.flow.dto.securityDto;

public class AuthenticationDto {

    private String login;
    private String password;

    // Constructeur
    public AuthenticationDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // Getters
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
