package com.eldar.java_challenge_2025.Web;

import com.eldar.java_challenge_2025.Application.Services.IAccountService;
import com.eldar.java_challenge_2025.Domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> create(@RequestParam String accountNumber) {
       return ResponseEntity.ok(accountService.createAccount(accountNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable("id") Long id,
                                           @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(accountService.deposit(id, amount));
    }

    @PutMapping("/{id}/extract")
    public ResponseEntity<Account> extract(@PathVariable("id") Long id, @RequestParam BigDecimal amount) {
       return ResponseEntity.ok(accountService.extract(id, amount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable("id") Long id) {
         return ResponseEntity.ok(accountService.getById(id));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
       return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAll() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }
}
