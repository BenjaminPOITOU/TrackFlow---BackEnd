package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.MusicianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * @file REST controller for managing a musician's own profile.
 */

/**
 * Handles protected endpoints related to the authenticated musician's profile.
 * Access to these endpoints requires a valid JWT with the 'ROLE_MUSICIAN' authority.
 */
@RestController
@RequestMapping("/api/profile/musician")
@PreAuthorize("hasRole('MUSICIAN')")
public class MusicianProfileController {

    private final MusicianService musicianService;
    private final UserRepository userRepository;

    @Autowired
    public MusicianProfileController(MusicianService musicianService, UserRepository userRepository) {
        this.musicianService = musicianService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the profile of the currently authenticated musician.
     * @param userDetails Provided by Spring Security, contains the login of the current user.
     * @return The musician's profile information.
     */
    @GetMapping("/me")
    public ResponseEntity<MusicianViewDto> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = findUserByDetails(userDetails);
        MusicianViewDto profile = musicianService.getMusicianById(currentUser.getId());
        return ResponseEntity.ok(profile);
    }

    /**
     * Updates the profile of the currently authenticated musician.
     * @param userDetails The details of the authenticated user.
     * @param updateDto The data to update.
     * @return The updated musician's profile.
     *
     */
    @PutMapping("/me")
    public ResponseEntity<MusicianViewDto> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MusicianUpdateDto updateDto) {
        User currentUser = findUserByDetails(userDetails);
        MusicianViewDto updatedProfile = musicianService.updateMusician(currentUser.getId(), updateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Deletes the account of the currently authenticated musician.
     * @param userDetails The details of the authenticated user.
     * @return A response with no content, indicating success.
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = findUserByDetails(userDetails);
        musicianService.deleteMusician(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    private User findUserByDetails(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }
}