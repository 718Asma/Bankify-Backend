package com.ebanking.backend.dto;

import java.time.LocalDate;

public record ModifierProfilRequest(
    String nom,
    String prenom,
    String email,
    LocalDate dateNaiss,
    String adresse,
    Integer telephone
) {}