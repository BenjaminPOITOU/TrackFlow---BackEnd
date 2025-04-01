package com.eql.cda.track.flow.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String userType, String userId, Long id) {
        super(userType);
    }
}
