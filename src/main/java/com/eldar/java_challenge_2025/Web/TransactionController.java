package com.eldar.java_challenge_2025.Web;


import com.eldar.java_challenge_2025.Application.DTO.Request.TransactionCreateDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.DuplicateCheckDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TopAccountDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TransactionDTO;
import com.eldar.java_challenge_2025.Application.Services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionCreateDTO dto) {
       return ResponseEntity.ok(transactionService.recordTransfer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@PathVariable("id") String transactionId) {
         return ResponseEntity.ok(transactionService.findById(transactionId));
    }

    @GetMapping("/top10")
    public ResponseEntity<List<TopAccountDTO>> top10() {
        return ResponseEntity.ok(transactionService.top10Accounts());
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<DuplicateCheckDTO> exists(@PathVariable("id") String transactionId) {
        boolean procesed = transactionService.isDuplicate(transactionId);
        String message = procesed ? "La transacción " + transactionId + " ya fue procesada."
                                    : "La transacción " + transactionId + " no fue procesada.";
        return ResponseEntity.ok(new DuplicateCheckDTO(transactionId, procesed, message));
    }

}