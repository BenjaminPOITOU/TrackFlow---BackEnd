package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.securityDto.AuthenticationDto;
import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import org.springframework.http.ResponseEntity;

public interface SecurityService {

    ResponseEntity<Object> register(RegistrationDto registrationDto);
    ResponseEntity<Object> authenticate(AuthenticationDto authenticationDto);

}
