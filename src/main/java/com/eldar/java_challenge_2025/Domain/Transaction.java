package com.eldar.java_challenge_2025.Domain;

import com.eldar.java_challenge_2025.Domain.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions",
        indexes = {
                @Index(name = "idx_tx_account_id_to", columnList = "transaction_account_id_to"),
                @Index(name = "idx_tx_account_id_from", columnList = "transaction_account_id_from"),
        })
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "transaction_account_id_from",
            referencedColumnName = "account_id",
            nullable = false
    )
    private Account transactionAccountFrom;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "transaction_account_id_to",
            referencedColumnName = "account_id",
            nullable = false
    )
    private Account transactionAccountTo;

    @Column(name = "transaction_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal transactionAmount;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "transaction_timestamp", nullable = false)
    private Instant transactionTimestamp;
}
