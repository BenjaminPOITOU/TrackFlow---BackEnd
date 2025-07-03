package com.eql.cda.track.flow.service;


import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;

/**
 * @file Service interface for business logic related to Musician users.
 */
public interface MusicianService {

    /**
     * Creates a new musician, encodes their password, and persists them.
     * @param createDto DTO containing the new musician's data.
     * @return A DTO representation of the newly created musician.
     */
    MusicianViewDto createMusician(MusicianCreateDto createDto);

    /**
     * Retrieves a musician's profile by their ID.
     * @param id The unique identifier of the musician.
     * @return A DTO of the musician's profile.
     */
    MusicianViewDto getMusicianById(Long id);

    /**
     * Updates an existing musician's profile.
     * @param id The ID of the musician to update.
     * @param updateDto DTO containing the fields to update.
     * @return A DTO of the updated musician profile.
     */
    MusicianViewDto updateMusician(Long id, MusicianUpdateDto updateDto);

    /**
     * Deletes a musician's account by their ID.
     * @param id The ID of the musician to delete.
     */
    void deleteMusician(Long id);
}