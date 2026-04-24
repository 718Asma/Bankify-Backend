package com.ebanking.backend.dto;

public record LoginRequest(
    String email, 
    String password, 
    String userType
) {}
// Client or Agent
