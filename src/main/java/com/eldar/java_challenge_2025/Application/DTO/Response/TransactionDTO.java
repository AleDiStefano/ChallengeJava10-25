package com.eldar.java_challenge_2025.Application.DTO.Response;


import com.eldar.java_challenge_2025.Domain.Enums.TransactionType;
import com.eldar.java_challenge_2025.Domain.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDTO(
        @NotNull @NotBlank String transactionId,
        @NotNull @NotBlank Long accountIdFrom,
        @NotNull @NotBlank Long accountId,
        @NotNull @NotBlank BigDecimal amount,
        @NotNull @NotBlank TransactionType type,
        @NotNull @NotBlank Instant timestamp
)
{
    public static TransactionDTO fromEntity(Transaction t) {
        return new TransactionDTO(
                t.getTransactionId(),
                t.getTransactionAccountFrom().getAccountId(),
                t.getTransactionAccountTo().getAccountId(),
                t.getTransactionAmount(),
                t.getTransactionType(),
                t.getTransactionTimestamp()
        );
    }
}