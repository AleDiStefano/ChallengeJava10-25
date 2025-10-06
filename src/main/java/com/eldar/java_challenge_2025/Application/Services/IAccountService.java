package com.eldar.java_challenge_2025.Application.Services;

import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Enums.Status;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService{
    Account createAccount(String accountNumber);
    void deleteAccount(Long id);
    Account deposit(Long id, BigDecimal amount);
    Account extract(Long id, BigDecimal amount);
    Account getById(Long id);
    Account getByAccountNumber(String accountNumber);
    List<Account> getAll();
    BigDecimal getBalance(Long id);
}
