package com.ebanking.backend.dto;

import java.time.LocalDate;
import java.util.Set;

public record TransactionResponse(
    Integer id,
    String type,
    LocalDate dateRealisation,
    Float montant,
    String statut,
    Integer agentCin,
    Set<String> compteRibs
) {}