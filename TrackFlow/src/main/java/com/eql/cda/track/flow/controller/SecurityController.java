package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.securityDto.AuthenticationDto;
import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import com.eql.cda.track.flow.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class SecurityController {

    private SecurityService securityService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationDto registrationDto) {
        // L'implémentation factice (ou réelle plus tard) de SecurityService gère la logique
        return securityService.register(registrationDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticateUser(@RequestBody AuthenticationDto authenticationDto) {
        // L'implémentation factice (ou réelle plus tard) de SecurityService gère la logique
        return securityService.authenticate(authenticationDto);
    }
    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
