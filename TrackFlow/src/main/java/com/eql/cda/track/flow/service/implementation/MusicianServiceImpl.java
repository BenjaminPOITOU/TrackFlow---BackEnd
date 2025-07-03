package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.entity.Musician;

import com.eql.cda.track.flow.entity.ProfileType;
import com.eql.cda.track.flow.entity.SecurityRole;
import com.eql.cda.track.flow.exception.UserAlreadyExistsException;
import com.eql.cda.track.flow.repository.MusicianRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.MusicianService;
import com.eql.cda.track.flow.service.mapper.MusicianMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @file Implementation of the MusicianService interface.
 */

/**
 * This service contains the business logic for managing musician users.
 * It handles data validation, password encoding, and interaction with the repositories.
 */
@Service
@Transactional
public class MusicianServiceImpl implements MusicianService {

    private final MusicianRepository musicianRepository;
    private final UserRepository userRepository;
    private final MusicianMapper musicianMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MusicianServiceImpl(MusicianRepository musicianRepository, UserRepository userRepository, MusicianMapper musicianMapper, PasswordEncoder passwordEncoder) {
        this.musicianRepository = musicianRepository;
        this.userRepository = userRepository;
        this.musicianMapper = musicianMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new musician after validating that the login does not already exist.
     *
     * @param createDto The DTO containing the data for the new musician.
     * @return A DTO view of the newly created musician.
     * @throws UserAlreadyExistsException if a user with the same login already exists.
     */
    @Override
    public MusicianViewDto createMusician(MusicianCreateDto createDto) {
        if (userRepository.existsByLogin(createDto.getLogin())) {
            throw new UserAlreadyExistsException(createDto.getLogin());
        }

        Musician musician = musicianMapper.toEntity(createDto);
        musician.setPassword(passwordEncoder.encode(createDto.getPassword()));


        musician.setProfileType(ProfileType.MUSICIAN);
        musician.setSecurityRole(SecurityRole.ROLE_MUSICIAN);

        Musician savedMusician = musicianRepository.save(musician);
        return musicianMapper.toViewDto(savedMusician);
    }

    @Override
    @Transactional(readOnly = true)
    public MusicianViewDto getMusicianById(Long id) {
        Musician musician = musicianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Musician not found with id: " + id));
        return musicianMapper.toViewDto(musician);
    }

    @Override
    public MusicianViewDto updateMusician(Long id, MusicianUpdateDto updateDto) {
        Musician musician = musicianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Musician not found with id: " + id));

        musicianMapper.updateFromDto(updateDto, musician);
        Musician updatedMusician = musicianRepository.save(musician);
        return musicianMapper.toViewDto(updatedMusician);
    }

    @Override
    public void deleteMusician(Long id) {
        if (!musicianRepository.existsById(id)) {
            throw new EntityNotFoundException("Musician not found with id: " + id);
        }
        musicianRepository.deleteById(id);
    }
}