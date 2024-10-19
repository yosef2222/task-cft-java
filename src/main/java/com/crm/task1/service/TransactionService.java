package com.crm.task1.service;

import com.crm.task1.model.Seller;
import com.crm.task1.model.Transaction;
import com.crm.task1.repository.SellerRepository;
import com.crm.task1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new IllegalStateException("Transaction not found"));
    }

    @Autowired
    private SellerRepository sellerRepository;

    public Transaction createTransaction(Long sellerId, Transaction transaction) {
        Optional<Seller> seller = sellerRepository.findById(sellerId);
        if (seller.isEmpty()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }

        transaction.setSeller(seller.get());

        String paymentType = transaction.getPaymentType().toUpperCase();

        if (!paymentType.equals("CASH") && !paymentType.equals("CARD") && !paymentType.equals("TRANSFER")){
            throw new RuntimeException(paymentType + " is not a valid value, only CASH, CARD or TRANSFER are");
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }
}
