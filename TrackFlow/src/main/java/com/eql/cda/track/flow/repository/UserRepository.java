package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @file Spring Data JPA repository for the base User entity.
 */

/**
 * This repository provides data access methods for {@link User} entities.
 * It includes crucial methods for Spring Security to find a user by their login
 * and to efficiently check for the existence of a login.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique login identifier.
     * This method is essential for the authentication process.
     *
     * @param login The login username to search for.
     * @return An {@link Optional} containing the found user, or empty if not found.
     */
    Optional<User> findByLogin(String login);

    /**
     * Checks if a user with the specified login identifier already exists.
     * This is a performance-optimized method for validation purposes, as it
     * avoids loading the full entity from the database.
     *
     * @param login The login username to check for existence.
     * @return {@code true} if a user with the given login exists, {@code false} otherwise.
     */
    boolean existsByLogin(String login);
}