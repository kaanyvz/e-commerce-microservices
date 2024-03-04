package com.ky.userservice.util;

import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.response.AuthenticationResponse;
import com.ky.userservice.service.security.JWTService;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    private final JWTService jwtService;

    public AuthenticationHelper(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthenticationResponse getAuthResponse(UserPrincipal userPrincipal){
        return new AuthenticationResponse(jwtService.generateToken(userPrincipal), jwtService.generateRefreshToken(userPrincipal));
    }
}
