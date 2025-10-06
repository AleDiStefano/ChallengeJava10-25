package com.eldar.java_challenge_2025.Application.DTO.Response;

public record CsvRowResultDTO(
        int lineNumber,
        String transactionId,
        boolean success,
        String message
) {}