package com.crm.task1.repository;

import com.crm.task1.model.Seller;
import com.crm.task1.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Long sellerId);
    List<Transaction> findAllByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findAllBySeller(Seller seller);

}