package com.eldar.java_challenge_2025.Application.DTO.Response;

import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Enums.Status;
import java.math.BigDecimal;

public record AccountDTO(Long id, String accountNumber, BigDecimal balance, Status status)
{
    public static AccountDTO fromEntity(Account a) {
        return new AccountDTO(
                a.getAccountId(),
                a.getAccountNumber(),
                a.getAccountBalance(),
                a.getAccountStatus()
        );
    }
}