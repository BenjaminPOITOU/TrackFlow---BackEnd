package com.eql.cda.track.flow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when an attempt is made to register a user
 * with a login (e.g., email) that is already present in the system.
 * This exception carries the conflicting login for detailed error reporting.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

    private final String login;

    /**
     * Constructs a new UserAlreadyExistsException with the conflicting login.
     * @param login The login that already exists in the system.
     */
    public UserAlreadyExistsException(String login) {
        super("User with login '" + login + "' already exists.");
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}