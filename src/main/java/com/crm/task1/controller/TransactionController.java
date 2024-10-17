package com.crm.task1.controller;

import com.crm.task1.model.Transaction;
import com.crm.task1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestParam Long sellerId, @RequestBody Transaction transaction) {
        return transactionService.createTransaction(sellerId, transaction);
    }

    @GetMapping("/seller/{sellerId}")
    public List<Transaction> getTransactionsBySeller(@PathVariable Long sellerId) {
        return transactionService.getTransactionsBySeller(sellerId);
    }
}
