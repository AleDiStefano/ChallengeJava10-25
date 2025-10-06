package com.eldar.java_challenge_2025.Application.Services;


import com.eldar.java_challenge_2025.Application.DTO.Request.TransactionCreateDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TopAccountDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TransactionDTO;

import java.util.List;

public interface ITransactionService {
    TransactionDTO recordTransfer(TransactionCreateDTO dto);
    boolean isDuplicate(String transactionId);
    List<TopAccountDTO> top10Accounts();
    TransactionDTO findById(String transactionId);
}