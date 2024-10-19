package com.crm.task1.service;

import com.crm.task1.model.Period;
import com.crm.task1.model.Seller;
import com.crm.task1.model.Transaction;
import com.crm.task1.repository.SellerRepository;
import com.crm.task1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public Seller findMostProductiveSeller(LocalDateTime start, LocalDateTime end) {
        Map<Seller, BigDecimal> totalAmounts = new HashMap<>();
        transactionRepository.findAllByTransactionDateBetween(start, end)
                .forEach(transaction -> totalAmounts.compute(transaction.getSeller(),
                        (seller, amount) -> amount == null ? transaction.getAmount() : amount.add(transaction.getAmount())));

        Seller mostProductiveSeller = null;
        BigDecimal maxAmount = BigDecimal.ZERO;
        for (Map.Entry<Seller, BigDecimal> entry : totalAmounts.entrySet()) {
            if (entry.getValue().compareTo(maxAmount) > 0) {
                maxAmount = entry.getValue();
                mostProductiveSeller = entry.getKey();
            }
        }
        return mostProductiveSeller;
    }

    public List<Seller> findSellersWithTotalAmountLessThan(LocalDateTime start, LocalDateTime end, BigDecimal threshold) {
        Map<Seller, BigDecimal> totalAmounts = new HashMap<>();

        List<Seller> allSellers = sellerRepository.findAll();
        allSellers.forEach(seller -> totalAmounts.put(seller, BigDecimal.ZERO));

        transactionRepository.findAllByTransactionDateBetween(start, end)
                .forEach(transaction -> totalAmounts.compute(transaction.getSeller(),
                        (seller, amount) -> amount.add(transaction.getAmount())));

        return totalAmounts.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(threshold) < 0)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Period findBestPeriodForSeller(Long sellerId) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
        if (sellerOptional.isEmpty()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        Seller seller = sellerOptional.get();
        List<Transaction> transactions = transactionRepository.findAllBySeller(seller);

        if (transactions.isEmpty()) {
            return null; // No transactions found
        }


        transactions.sort(Comparator.comparing(Transaction::getTransactionDate));


        LocalDateTime bestStartDate = transactions.get(0).getTransactionDate();
        LocalDateTime bestEndDate = transactions.get(0).getTransactionDate();
        int maxTransactions = 0;

        LocalDate currentDay = transactions.get(0).getTransactionDate().toLocalDate();
        int transactionsOnCurrentDay = 1;

        for (int i = 1; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();

            if (transactionDate.isEqual(currentDay)) {
                transactionsOnCurrentDay++;
            } else {
                if (transactionsOnCurrentDay > maxTransactions) {
                    maxTransactions = transactionsOnCurrentDay;
                    bestStartDate = transactions.get(i - 1).getTransactionDate();
                    bestEndDate = transactions.get(i - 1).getTransactionDate().plusDays(1);
                }

                currentDay = transactionDate;
                transactionsOnCurrentDay = 1;
            }
        }

        if (transactionsOnCurrentDay > maxTransactions) {
            bestStartDate = transactions.get(transactions.size() - 1).getTransactionDate();
            bestEndDate = transactions.get(transactions.size() - 1).getTransactionDate().plusDays(1);
        }

        return new Period(bestStartDate, bestEndDate);
    }

}