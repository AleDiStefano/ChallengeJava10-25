package com.eldar.java_challenge_2025.Application.DTO.Request;

import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Enums.TransactionType;
import com.eldar.java_challenge_2025.Domain.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.math.BigDecimal;

public record TransactionCreateDTO(@NotNull @NotBlank String transactionId,
                                   @NotNull @NotBlank Long accountIdFrom,
                                   @NotNull @NotBlank Long accountId,
                                   @NotNull @NotBlank BigDecimal amount,
                                   @NotNull @NotBlank TransactionType type,
                                   @NotNull @NotBlank String timestamp) {

    public Transaction toEntity(Account from, Account to) {
        return Transaction.builder()
                .transactionId(transactionId)
                .transactionAccountFrom(from)
                .transactionAccountTo(to)
                .transactionAmount(amount)
                .transactionType(type)
                .transactionTimestamp(Instant.parse(timestamp.trim()))
                .build();
    }
}
