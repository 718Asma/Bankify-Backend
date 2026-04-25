package com.ebanking.backend.dto;

public record ReinitialiserMotDePasseRequest(
    String ancienMotDePasse,
    String nouveauMotDePasse
) {}