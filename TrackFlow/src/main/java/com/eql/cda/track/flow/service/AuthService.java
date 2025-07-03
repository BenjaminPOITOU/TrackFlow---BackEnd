package com.eql.cda.track.flow.service;


import com.eql.cda.track.flow.dto.auth.LoginDto;
import com.eql.cda.track.flow.dto.auth.RegisterDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<Object> register(RegisterDto registerDto);
    ResponseEntity<Object> authenticate(LoginDto loginDto);

}