package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Musician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @file Spring Data JPA repository for the Musician entity.
 */

/**
 * This repository provides data access methods for {@link Musician} entities.
 * It leverages Spring Data JPA to automatically generate implementations for
 * standard CRUD operations.
 */
@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long> {

}