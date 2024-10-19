package com.crm.task1.controller;

import com.crm.task1.model.Period;
import com.crm.task1.model.Seller;
import com.crm.task1.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/most-productive-seller")
    public ResponseEntity<Seller> getMostProductiveSeller(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Seller mostProductiveSeller = analyticsService.findMostProductiveSeller(startDate, endDate);
        return ResponseEntity.ok(mostProductiveSeller);
    }

    @GetMapping("/sellers-with-total-amount-less-than")
    public ResponseEntity<List<Seller>> getSellersWithTotalAmountLessThan(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("threshold") BigDecimal threshold) {
        List<Seller> sellers = analyticsService.findSellersWithTotalAmountLessThan(startDate, endDate, threshold);
        return ResponseEntity.ok(sellers);
    }


    @GetMapping("/best-period-for-seller")
    public ResponseEntity<Period> getBestPeriodForSeller(
            @RequestParam("sellerId") Long sellerId) {
        Period bestPeriod = analyticsService.findBestPeriodForSeller(sellerId);
        return ResponseEntity.ok(bestPeriod);
    }
}