package com.hecto.fitnessuniv.Transaction;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired private TransactionService transactionService;

    @PostMapping("/save")
    public Transaction saveTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @GetMapping("/{userId}")
    public Optional<Transaction> getTransactionByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionByUserId(userId);
    }
}
