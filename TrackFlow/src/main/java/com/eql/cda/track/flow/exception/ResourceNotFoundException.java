package com.eql.cda.track.flow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when a requested resource could not be found in the system.
 * This results in a HTTP 404 Not Found response, handled automatically by Spring.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * Use this for custom error messages.
     *
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with a standardized message.
     * Use this for common "not found by ID" scenarios.
     *
     * @param resourceName the name of the resource that was not found (e.g., "Version", "Annotation").
     * @param identifier   the identifier used to look up the resource (e.g., a Long ID).
     */
    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(String.format("%s not found with id: %s", resourceName, identifier));
    }
}