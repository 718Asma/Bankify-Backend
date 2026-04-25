package com.ebanking.backend.dto;

public record CreerCompteRequest(
    String rib,
    Float soldeInitial,
    String type,       // COURANT, EPARGNE
    Integer clientCin
) {}