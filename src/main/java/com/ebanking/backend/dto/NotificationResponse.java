package com.ebanking.backend.dto;

import java.time.LocalDate;

public record NotificationResponse(
    Integer id,
    String contenu,
    LocalDate dateCreation
) {}