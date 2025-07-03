package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.auth.LoginDto;
import com.eql.cda.track.flow.dto.auth.RegisterDto;
import com.eql.cda.track.flow.security.SecurityUser;
import com.eql.cda.track.flow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling public, non-authenticated user operations
 * such as registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService securityService;

    /**
     * Constructs a new AuthController with the required SecurityService.
     *
     * @param securityService The service responsible for authentication and registration logic.
     */
    public AuthController(AuthService securityService) {
        this.securityService = securityService;
    }

    /**
     * Authenticates a user based on their credentials and returns a JWT upon success.
     *
     * @param loginDto A DTO containing the user's login and password.
     * @return A ResponseEntity containing an {@link com.eql.cda.track.flow.dto.auth.AuthResponseDto} with the JWT.
     *         Returns a 401 Unauthorized status on failure.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto) {
        return securityService.authenticate(loginDto);
    }

    /**
     * Registers a new user in the system based on the provided account type.
     * This single endpoint handles the registration for all types of users (e.g., Musician, Listener).
     *
     * @param registerDto A DTO containing all necessary information for creating a new user account,
     *                    including the account type.
     * @return A ResponseEntity with the details of the newly created user profile and a 201 Created status.
     *         Returns a 400 Bad Request status if registration fails (e.g., login already exists).
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto) {
        return securityService.register(registerDto);
    }


        /**
         * Retrieves the details of the currently authenticated user.
         * The user is identified via the JWT provided in the Authorization header.
         *
         * @param principal The security principal object representing the logged-in user.
         * @return A ResponseEntity containing the user's ID, login, and authorities.
         */
        @GetMapping("/me")
        public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal SecurityUser principal) {
            if (principal == null) {
                return ResponseEntity.status(401).build();
            }

            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("id", principal.getId());
            userDetails.put("login", principal.getUsername());
            userDetails.put("roles", principal.getAuthorities());

            return ResponseEntity.ok(userDetails);
        }

    }