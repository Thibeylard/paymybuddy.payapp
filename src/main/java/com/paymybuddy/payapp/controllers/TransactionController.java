package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class TransactionController {

    @GetMapping("/transactions")
    public ResponseEntity<Collection<Transaction>> getUserTransactions(@RequestParam(name = "userID") int userID) {
        return null;
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> makeTransaction(@RequestParam(name = "userID") int userID,
                                                  @RequestParam(name = "recipientMail") String recipientMail,
                                                  @RequestParam(name = "amount") double amount,
                                                  @RequestParam(name = "description") String description) {
        return null;
    }
}
