package com.igor.n26.controller;

import com.igor.n26.model.Transaction;
import com.igor.n26.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * All-in-one RESTful controller for transaction statistics.
 *
 * @author Igor Shevchenko
 */
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostMapping("/transactions")
    public ResponseEntity postTransaction(@RequestBody Transaction transaction) {
        if (statisticsService.processTransaction(transaction)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/statistics")
    public ResponseEntity getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

}
