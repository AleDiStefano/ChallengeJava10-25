package com.eldar.java_challenge_2025.Application.DTO.Response;

public record DuplicateCheckDTO(String transactionId, boolean exists, String message) {}