package com.eql.cda.track.flow.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * A servlet filter that logs critical headers for incoming requests for debugging purposes.
 * It is ordered to run early in the filter chain to inspect requests before they are processed
 * by security filters or controllers.
 */
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    /**
     * Intercepts incoming requests to log the Authorization and Cookie headers.
     * This is useful for debugging authentication and session issues between a
     * frontend and the backend API.
     *
     * @param request The servlet request.
     * @param response The servlet response.
     * @param chain The filter chain to pass the request along.
     * @throws IOException If an I/O error occurs during the processing of the request.
     * @throws ServletException If the request could not be handled.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String authHeader = req.getHeader("Authorization");
        String cookieHeader = req.getHeader("Cookie");

        log.info("--- [Spring Boot Server] Incoming Request to [{} {}] ---", req.getMethod(), req.getRequestURI());

        if (authHeader != null && !authHeader.isEmpty()) {
            log.info(" > Authorization Header found: {}", authHeader);
        } else {
            log.warn(" > Authorization Header: NOT FOUND or empty.");
        }

        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            log.info(" > Cookie Header found: {}", cookieHeader);
        } else {
            log.warn(" > Cookie Header: NOT FOUND or empty.");
        }

        log.info("----------------------------------------------------------------------");

        chain.doFilter(request, response);
    }
}