package com.eldar.java_challenge_2025.Domain;

import com.eldar.java_challenge_2025.Domain.Enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "accounts",
        indexes = {
                @Index(name = "idx_account_number", columnList = "account_number", unique = true)
        }
)
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number",nullable = false, length = 64, unique = true)
    private String accountNumber;

    @Column(name = "account_balance",nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "account_status", nullable = false)
    @Builder.Default
    private Status accountStatus = Status.ACTIVE;

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser positivo");
        }
        this.accountBalance = this.accountBalance.add(amount);
    }

    public void extract(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto a extraer debe ser positivo");
        }
        this.accountBalance = this.accountBalance.subtract(amount);
    }
}
