package com.ebanking.backend.dto;

import java.time.LocalDate;

public record CompteResponse(
    String rib,
    Float solde,
    LocalDate dateCreation,
    String type,
    String status,
    Integer clientCin
) {}