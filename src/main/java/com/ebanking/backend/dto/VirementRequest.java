package com.ebanking.backend.dto;

public record VirementRequest(
    String ribDestination,
    Float montant
) {}