package com.ebanking.backend.service;

import com.ebanking.backend.dto.AuthResponse;
import com.ebanking.backend.dto.LoginRequest;
import com.ebanking.backend.dto.SignupRequest;

public interface AuthService {
    public AuthResponse signup(SignupRequest req);
    public AuthResponse login(LoginRequest req);
}
