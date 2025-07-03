package com.eql.cda.track.flow.dto.auth;

import com.eql.cda.track.flow.entity.ProfileType; // Assurez-vous que l'import est correct
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A Data Transfer Object that encapsulates all the necessary data for a new user registration.
 * It uses the ProfileType to determine what kind of user account to create (e.g., MUSICIAN, LISTENER).
 */
public class RegisterDto {

    @NotBlank(message = "Login is required.")
    @Email(message = "Login must be a valid email address.")
    @Size(max = 100, message = "Login cannot exceed 100 characters.")
    private String login;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name cannot exceed 50 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name cannot exceed 50 characters.")
    private String lastName;

    @NotNull(message = "Profile type must be specified (e.g., MUSICIAN, LISTENER).")
    private ProfileType profileType;

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

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }
}