package com.eql.cda.track.flow.dto.userDto.listennerDto;

import com.eql.cda.track.flow.entity.UserRole;

public class ListennerProfileUpdateDto {

    private String firstName;
    private String lastName;
    private UserRole userRole;


    public ListennerProfileUpdateDto() {
    }

    public ListennerProfileUpdateDto(String firstName, String lastName, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public UserRole getUserRole() {
        return userRole;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
