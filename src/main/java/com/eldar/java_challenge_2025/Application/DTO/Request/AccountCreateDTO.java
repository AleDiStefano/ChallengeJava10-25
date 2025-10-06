package com.eldar.java_challenge_2025.Application.DTO.Request;

import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Enums.Status;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AccountCreateDTO(@NotBlank String accountNumber)
{
    public Account toEntity() {
        return Account.builder()
                .accountNumber(accountNumber)
                .accountBalance(BigDecimal.ZERO)
                .accountStatus(Status.ACTIVE)
                .build();
    }
}