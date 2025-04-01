package com.eql.cda.track.flow.dto.securityDto;

import com.eql.cda.track.flow.dto.userDto.AccountType;
import com.eql.cda.track.flow.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistrationDto {


    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole userRole;

    @NotNull(message = "Account type must be specified (MUSICIAN or LISTENER)")
    private AccountType accountType;


    // --- Getters ---

    @NotNull
    @NotEmpty
    @Email
    public String getLogin() {
        return login;
    }

    @NotNull
    @NotEmpty
    @Size(min = 8)
    public String getPassword() {
        return password;
    }

    @NotNull
    @NotEmpty
    public String getFirstName() {
        return firstName;
    }

    @NotNull
    @NotEmpty
    public String getLastName() {
        return lastName;
    }

    @NotNull
    public UserRole getUserRole() {
        return userRole;
    }



    // --- Setters ---

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
}

