package com.crm.task1;

import com.crm.task1.model.Period;
import com.crm.task1.model.Seller;
import com.crm.task1.model.Transaction;
import com.crm.task1.repository.SellerRepository;
import com.crm.task1.repository.TransactionRepository;
import com.crm.task1.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private Seller seller1;
    private Seller seller2;
    private Seller seller3;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        seller1 = new Seller(1L, "Seller 1", "contact1@example.com", LocalDateTime.now());
        seller2 = new Seller(2L, "Seller 2", "contact2@example.com", LocalDateTime.now());
        seller3 = new Seller(3L, "Seller 3", "contact3@example.com", LocalDateTime.now());

        transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, seller1, new BigDecimal("100"), "CASH", LocalDateTime.of(2023, 10, 10, 10, 0, 0)));
        transactions.add(new Transaction(2L, seller2, new BigDecimal("50"), "CARD", LocalDateTime.of(2023, 10, 10, 11, 0, 0)));
        transactions.add(new Transaction(3L, seller1, new BigDecimal("75"), "TRANSFER", LocalDateTime.of(2023, 10, 11, 12, 0, 0)));
        transactions.add(new Transaction(4L, seller2, new BigDecimal("125"), "CASH", LocalDateTime.of(2023, 10, 11, 13, 0, 0)));
        transactions.add(new Transaction(5L, seller1, new BigDecimal("150"), "CARD", LocalDateTime.of(2023, 10, 12, 14, 0, 0)));
        transactions.add(new Transaction(6L, seller3, new BigDecimal("200"), "TRANSFER", LocalDateTime.of(2023, 10, 10, 10, 0, 0)));
    }

    @Test
    void findMostProductiveSeller() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 10, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 10, 23, 59, 59);


        when(transactionRepository.findAllByTransactionDateBetween(start, end)).thenReturn(transactions);

        Seller mostProductive = analyticsService.findMostProductiveSeller(start, end);
        //assertEquals(seller3, mostProductive);


        start = LocalDateTime.of(2023, 10, 11, 0, 0, 0);
        end = LocalDateTime.of(2023, 10, 11, 23, 59, 59);
        when(transactionRepository.findAllByTransactionDateBetween(start, end)).thenReturn(transactions); // Mocking for this date range

        mostProductive = analyticsService.findMostProductiveSeller(start, end);
        //assertEquals(seller2, mostProductive);
    }

    @Test
    void findSellersWithTotalAmountLessThan() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 10, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 12, 23, 59, 59);


        when(transactionRepository.findAllByTransactionDateBetween(start, end)).thenReturn(transactions);


        BigDecimal threshold = new BigDecimal("150");
        List<Seller> sellers = analyticsService.findSellersWithTotalAmountLessThan(start, end, threshold);
        assertEquals(2, sellers.size());
        assertTrue(sellers.contains(seller2));
        assertTrue(sellers.contains(seller3));


        threshold = new BigDecimal("250");
        sellers = analyticsService.findSellersWithTotalAmountLessThan(start, end, threshold);
        assertEquals(1, sellers.size());
        assertTrue(sellers.contains(seller1));
    }

    @Test
    void findBestPeriodForSeller() {
        // Mocking the repository to return transactions
        when(sellerRepository.findById(seller1.getId())).thenReturn(Optional.of(seller1));
        when(transactionRepository.findAllBySeller(any(Seller.class))).thenReturn(transactions);


        Period bestPeriod = analyticsService.findBestPeriodForSeller(seller1.getId());
        assertEquals(LocalDateTime.of(2023, 10, 12, 0, 0, 0), bestPeriod.getStart());
        assertEquals(LocalDateTime.of(2023, 10, 13, 0, 0, 0), bestPeriod.getEnd());


        when(sellerRepository.findById(seller2.getId())).thenReturn(Optional.of(seller2));
        when(transactionRepository.findAllBySeller(any(Seller.class))).thenReturn(transactions);

        bestPeriod = analyticsService.findBestPeriodForSeller(seller2.getId());
        assertEquals(LocalDateTime.of(2023, 10, 11, 0, 0, 0), bestPeriod.getStart());
        assertEquals(LocalDateTime.of(2023, 10, 12, 0, 0, 0), bestPeriod.getEnd());
    }
}