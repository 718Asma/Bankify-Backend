package com.ebanking.backend.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    public String generateToken(UserDetails userDetails, String role);

    public String extractUsername(String token);

    public String extractRole(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
}
