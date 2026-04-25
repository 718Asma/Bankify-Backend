package com.ebanking.backend.dto;

public record PasswordResetRequest(
    String oldPassword,
    String newPassword
) {}
