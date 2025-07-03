package com.eql.cda.track.flow.dto.auth;

/**
 * @file A DTO to send back an authentication token to the client.
 */

/**
 * A Data Transfer Object that provides the authentication token to the client upon
 * successful login. It typically includes the access token and the token type (e.g., "Bearer").
 * This structured response makes it easy for the client to handle the token.
 */
public class AuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer ";

    /**
     * Constructs a new AuthResponseDto with the given access token.
     * @param accessToken The JWT generated for the authenticated user.
     */
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}