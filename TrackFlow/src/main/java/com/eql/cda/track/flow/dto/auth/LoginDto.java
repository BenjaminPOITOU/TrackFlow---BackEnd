package com.eql.cda.track.flow.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @file A DTO for handling user login requests.
 */

/**
 * A Data Transfer Object that encapsulates the credentials required for a user to log in.
 * It includes validation constraints to ensure that the required fields are provided
 * by the client before processing the authentication request.
 */
public class LoginDto {

    @NotBlank(message = "Login is required.")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}