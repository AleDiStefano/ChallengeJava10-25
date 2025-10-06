package com.eldar.java_challenge_2025.Application.Services;

import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Enums.Status;
import com.eldar.java_challenge_2025.Infrastructure.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;

@Service
@Transactional
public class AccountService implements IAccountService{

    @Autowired
    private AccountRepository accountRepository;

    
    @Override
    public Account createAccount(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new IllegalArgumentException("Ya existe una cuenta con número: " + accountNumber);
        }
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .build();
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        account.setAccountStatus(Status.INACTIVE);
        accountRepository.save(account);
    }

    @Override
    public Account deposit(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        account.deposit(amount);
        return accountRepository.save(account);
    }

    @Override
    public Account extract(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        account.extract(amount);
        return accountRepository.save(account);
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
    }

    @Override
    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + accountNumber));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada: " + id));
        return account.getAccountBalance();
    }
}
