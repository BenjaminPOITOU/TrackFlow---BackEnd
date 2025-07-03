package com.eql.cda.track.flow.security;

import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @file Custom implementation of Spring Security's UserDetailsService.
 */

/**
 * This service is responsible for loading user-specific data from the database.
 * Spring Security uses this service during the authentication process to create a
 * custom {@link SecurityUser} principal, which includes the user's ID.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username (in our case, the 'login' field)
     * and wraps it in a custom SecurityUser object.
     *
     * @param login the username identifying the user whose data is required.
     * @return a fully populated {@link SecurityUser} record (never {@code null}).
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));

        return new SecurityUser(user);
    }
}