package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.auth.AuthResponseDto;
import com.eql.cda.track.flow.dto.auth.LoginDto;
import com.eql.cda.track.flow.dto.auth.RegisterDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.security.JwtTokenProvider;
import com.eql.cda.track.flow.security.SecurityUser;
import com.eql.cda.track.flow.service.AuthService;
import com.eql.cda.track.flow.service.MusicianService;
import com.eql.cda.track.flow.service.mapper.AuthMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implements the authentication and registration logic for the application.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final MusicianService musicianService;
    private final JwtTokenProvider tokenProvider;
    private final AuthMapper authMapper;

    /**
     * Constructs a new AuthServiceImpl with the required dependencies.
     * @param authenticationManager The manager for processing authentication requests.
     * @param musicianService The service for musician-related operations.
     * @param tokenProvider The utility for generating JWTs.
     * @param authMapper The mapper for converting DTOs.
     */
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, MusicianService musicianService, JwtTokenProvider tokenProvider, AuthMapper authMapper) {
        this.authenticationManager = authenticationManager;
        this.musicianService = musicianService;
        this.tokenProvider = tokenProvider;
        this.authMapper = authMapper;
    }

    /**
     * Authenticates a user with the provided credentials.
     * Upon success, it generates a JWT, sets it in a secure HttpOnly cookie,
     * and returns an AuthResponseDto containing the user's public information.
     *
     * @param loginDto The DTO containing the user's login and password.
     * @return A ResponseEntity with an AuthResponseDto in the body and a Set-Cookie header.
     */
    @Override
    public ResponseEntity<Object> authenticate(LoginDto loginDto) {
        log.info("[BACKEND-AUTH] 1. Attempting to authenticate user: {}", loginDto.getLogin());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getLogin(),
                            loginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[BACKEND-AUTH] 2. User {} authenticated successfully.", authentication.getName());

            String token = tokenProvider.generateToken(authentication);
            log.info("[BACKEND-AUTH] 3. Generated token value: [{}]", token);

            if (token == null || token.trim().isEmpty()) {
                log.error("[BACKEND-AUTH] 3a. FATAL ERROR: Token generation returned an empty or null string!");
                throw new IllegalStateException("Token generation failed for user " + authentication.getName());
            }

            ResponseCookie cookie = ResponseCookie.from("auth-token", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 60 * 24)
                    .sameSite("Lax")
                    .build();

            log.info("[BACKEND-AUTH] 4. Created HttpOnly cookie. Sending response to client.");
            SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponseDto(token));
        }catch (BadCredentialsException e){
            log.warn("[BACKEND-AUTH] Invalid login attempt for user: {}", loginDto.getLogin());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    /**
     * Registers a new user based on the provided profile type.
     * This method delegates the creation logic to the appropriate service.
     *
     * @param registerDto The DTO containing registration details.
     * @return A ResponseEntity with the created user's data or an error message.
     */
    @Override
    public ResponseEntity<Object> register(RegisterDto registerDto) {
        switch (registerDto.getProfileType()) {
            case MUSICIAN:
                MusicianCreateDto createDto = authMapper.toMusicianCreateDto(registerDto);
                MusicianViewDto newMusician = musicianService.createMusician(createDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(newMusician);
            default:
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Invalid profile type provided."));
        }
    }
}