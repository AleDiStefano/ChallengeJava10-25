package com.eldar.java_challenge_2025.Infrastructure;

import com.eldar.java_challenge_2025.Domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createUsers(){

        for (int i = 0;i < 29;i++){
            Account account = Account.builder()
                    .accountNumber("ACC-%03d".formatted(i + 1))
                    .build();
            account.deposit(BigDecimal.valueOf(1000000));
            accountRepository.save(account);
        }

    }
}