package com.ebanking.backend.dto;

public record AuthResponse(
    String token,
    String role,
    String email,
    String nom,
    String prenom
) {}