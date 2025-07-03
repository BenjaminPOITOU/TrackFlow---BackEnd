package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.entity.Musician;
import com.eql.cda.track.flow.entity.SecurityRole;
import org.springframework.stereotype.Component;

/**
 * @file A mapper component responsible for converting between Musician entities and their DTOs.
 */

/**
 * This mapper handles the conversion logic between the {@link Musician} entity and its
 * various Data Transfer Objects ({@link MusicianCreateDto}, {@link MusicianViewDto},
 * {@link MusicianUpdateDto}). It ensures that the security role is set correctly
 * during user creation and that data is properly mapped for client-side consumption
 * and updates.
 */
@Component
public class MusicianMapper {

    /**
     * Converts a {@link MusicianCreateDto} to a new {@link Musician} entity.
     * This method is responsible for setting both the profile type chosen by the user
     * and the default, non-negotiable security role for a new musician.
     * The password provided in the DTO is expected to be raw and will be encoded
     * in the service layer before persistence.
     *
     * @param dto The source {@link MusicianCreateDto} object containing registration data.
     * @return A new {@link Musician} entity, or {@code null} if the input DTO is null.
     */
    public Musician toEntity(MusicianCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Musician musician = new Musician();
        musician.setFirstName(dto.getFirstName());
        musician.setLastName(dto.getLastName());
        musician.setLogin(dto.getLogin());
        musician.setPassword(dto.getPassword());

        // Assign the UI-facing profile type from the registration form
        musician.setProfileType(dto.getProfileType());

        // Assign the system-level security role automatically. This is a fixed role.
        musician.setSecurityRole(SecurityRole.ROLE_MUSICIAN);

        return musician;
    }

    /**
     * Converts a {@link Musician} entity to a detailed {@link MusicianViewDto}.
     * This DTO is intended for displaying a musician's profile information to the client,
     * including their specific profile type.
     *
     * @param entity The source {@link Musician} entity.
     * @return A {@link MusicianViewDto}, or {@code null} if the input entity is null.
     */
    public MusicianViewDto toViewDto(Musician entity) {
        if (entity == null) {
            return null;
        }
        MusicianViewDto dto = new MusicianViewDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setBiography(entity.getBiography());
        dto.setPicture(entity.getPicture());
        dto.setProfileType(entity.getProfileType());
        dto.setCreationDate(entity.getCreationDate());

        return dto;
    }

    /**
     * Updates an existing {@link Musician} entity from a {@link MusicianUpdateDto}.
     * This method applies partial updates; it only changes fields in the entity
     * if they are not null in the source DTO.
     *
     * @param dto The source {@link MusicianUpdateDto} containing update data.
     * @param entity The target {@link Musician} entity to be modified.
     */
    public void updateFromDto(MusicianUpdateDto dto, Musician entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getMobile() != null) {
            entity.setMobile(dto.getMobile());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getBiography() != null) {
            entity.setBiography(dto.getBiography());
        }
        if (dto.getPicture() != null) {
            entity.setPicture(dto.getPicture());
        }
        if (dto.getProfileType() != null) {
            entity.setProfileType(dto.getProfileType());
        }
    }
}