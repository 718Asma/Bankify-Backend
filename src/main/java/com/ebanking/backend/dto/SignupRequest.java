package com.ebanking.backend.dto;

import java.time.LocalDate;

public record SignupRequest(
    Integer cin,
    String nom,
    String prenom,
    String email,
    String motDePasse,
    LocalDate dateNaiss,
    String adresse,
    Integer telephone,
    String userType,   // "CLIENT" or "AGENT"
    // Agent-specific fields — null if userType is CLIENT
    String poste,
    String agence
) {}