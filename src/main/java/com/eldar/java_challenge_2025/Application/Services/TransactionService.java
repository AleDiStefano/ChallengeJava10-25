package com.eldar.java_challenge_2025.Application.Services;

import com.eldar.java_challenge_2025.Application.DTO.Request.TransactionCreateDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TopAccountDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TransactionDTO;
import com.eldar.java_challenge_2025.Domain.Account;
import com.eldar.java_challenge_2025.Domain.Transaction;
import com.eldar.java_challenge_2025.Infrastructure.AccountRepository;
import com.eldar.java_challenge_2025.Infrastructure.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TransactionDTO recordTransfer(TransactionCreateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("El cuerpo de la transacción es obligatorio");
        if (transactionRepository.existsById(dto.transactionId())) {
            throw new IllegalArgumentException("Transacción duplicada: " + dto.transactionId());
        }
        if (dto.accountIdFrom() == null || dto.accountId() == null ||
                dto.accountIdFrom() == 0 || dto.accountId() == 0) {
            throw new IllegalArgumentException("Las cuentas origen/destino son obligatorias");
        }
        if (dto.accountIdFrom().equals(dto.accountId())) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser iguales");
        }
        if (dto.amount() == null || dto.amount().signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (dto.type() == null) throw new IllegalArgumentException("No especifica el tipo de transacción");
        if (dto.timestamp() == null || dto.timestamp().isBlank()) {
            throw new IllegalArgumentException("No especifica fecha");
        }

        Account accFrom = accountRepository.findById(dto.accountIdFrom())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta origen no encontrada: " + dto.accountIdFrom()));
        Account accTo = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Cuenta destino no encontrada: " + dto.accountId()));

        if (accFrom.getAccountBalance().compareTo(dto.amount()) < 0) {
            throw new IllegalArgumentException("Fondos insuficientes en la cuenta origen para hacer la transacción");
        }

        accFrom.extract(dto.amount());
        accTo.deposit(dto.amount());
        accountRepository.save(accFrom);
        accountRepository.save(accTo);

        Transaction tx = dto.toEntity(accFrom, accTo);
        tx.setTransactionId(dto.transactionId());
        tx.setTransactionTimestamp(Instant.parse(dto.timestamp().trim()));
        tx.setTransactionType(dto.type());

        Transaction saved = transactionRepository.save(tx);
        return TransactionDTO.fromEntity(saved);
    }

    @Override
    public boolean isDuplicate(String transactionId) {
        return transactionRepository.existsById(transactionId);
    }

    @Override
    public List<TopAccountDTO> top10Accounts() {
        List<Object[]> rows = transactionRepository.top10AccountsCombinedById();
        List<TopAccountDTO> result = new ArrayList<>();
        for (Object[] r : rows) {
            String account = (String) r[0];
            long total = ((Number) r[1]).longValue();
            result.add(new TopAccountDTO(account, total));
        }
        return result;
    }

    @Override
    public TransactionDTO findById(String transactionId) {
        Transaction t = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada: " + transactionId));
        return TransactionDTO.fromEntity(t);
    }
}
