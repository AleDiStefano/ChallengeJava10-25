package com.eldar.java_challenge_2025.Application.DTO.Response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountAmountDTO(@NotNull BigDecimal amount) {
}
