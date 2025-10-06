package com.eldar.java_challenge_2025.Application.DTO.Response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, Object> details
) {
    public static ErrorResponse create(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, null);
    }
    public static ErrorResponse create(int status, String error, String message, String path,
                                       Map<String, Object> details) {
        return new ErrorResponse(Instant.now(), status, error, message, path, details);
    }
}