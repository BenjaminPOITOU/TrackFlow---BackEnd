package com.eql.cda.track.flow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @file A utility component for handling JSON Web Token (JWT) operations using the legacy JJWT API.
 */

/**
 * This component is responsible for generating new JWTs upon successful authentication
 * and for validating and parsing incoming JWTs. This implementation uses the legacy
 * JJWT API which is more stable and widely supported.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LogManager.getLogger();

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationInMs;

    /**
     * Generates a JWT for a given authenticated user.
     * It uses the HS256 signature algorithm with the configured secret.
     *
     * @param authentication The authentication object containing the user's details.
     * @return A signed JWT as a non-empty string.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(jwtExpirationInMs, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    /**
     * Generates a JWT for a given username with roles.
     *
     * @param username The username.
     * @param roles List of user roles.
     * @return A signed JWT as a non-empty string.
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(jwtExpirationInMs, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT.
     *
     * @param token The JWT string.
     * @return The username contained within the token.
     */
    public String getUsernameFromJWT(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts all claims from a given JWT.
     *
     * @param token The JWT string.
     * @return All claims contained within the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    /**
     * Extracts a specific claim from a given JWT.
     *
     * @param token The JWT string.
     * @param claimsResolver Function to extract the specific claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the expiration date from a given JWT.
     *
     * @param token The JWT string.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Validates the integrity and expiration of a given JWT.
     *
     * @param token The JWT string to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Signature JWT invalide.", e);
        } catch (MalformedJwtException e) {
            logger.error("Token JWT invalide.", e);
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expiré.", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supporté.", e);
        } catch (IllegalArgumentException e) {
            logger.error("Arguments du token JWT invalides.", e);
        }
        return false;
    }

    /**
     * Validates a JWT token against user details.
     *
     * @param token The JWT string to validate.
     * @param userDetails The user details to validate against.
     * @return {@code true} if the token is valid for the user, {@code false} otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWT(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token The JWT string to check.
     * @return {@code true} if the token is expired, {@code false} otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts JWT token from HTTP request Authorization header.
     *
     * @param request The HTTP servlet request.
     * @return The JWT token string, or null if not found.
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}