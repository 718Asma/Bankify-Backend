package com.ebanking.backend.dto;

import java.time.LocalDate;

public record ClientResponse(
    Integer cin,
    String nom,
    String prenom,
    String email,
    String adresse,
    Integer telephone,
    LocalDate dateNaiss,
    String role
) {}