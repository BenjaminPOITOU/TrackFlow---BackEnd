package com.eql.cda.track.flow.entity;

/**
 * Defines the fundamental security roles within the application.
 * These roles dictate the primary navigation and authorization context for a user.
 * (e.g., MUSICIAN, LISTENER, ADMIN).
 */
public enum SecurityRole {
    ROLE_MUSICIAN,
    ROLE_LISTENER,
    ROLE_ADMIN;
}