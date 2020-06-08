package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Transaction;
import com.paymybuddy.payapp.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<Collection<Transaction>> getUserTransactions() {
        return null;
    }

    @GetMapping("/transactions/debit")
    public ResponseEntity<Collection<Transaction>> getUserDebitTransactions() {
        return null;
    }

    @GetMapping("/transactions/credit")
    public ResponseEntity<Collection<Transaction>> getUserCreditTransactions() {
        return null;
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> makeTransaction(@RequestParam(name = "recipientMail") String recipientMail,
                                                  @RequestParam(name = "amount") double amount,
                                                  @RequestParam(name = "description") String description) {
        return null;
    }
}
