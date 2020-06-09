package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Transaction;
import com.paymybuddy.payapp.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
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
        Collection<Transaction> result;
        Logger.debug("Requested transactions of current user");
        try {
            result = transactionService.getUserTransactions();
            Logger.info("Transactions of user found");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/debit")
    public ResponseEntity<Collection<Transaction>> getUserDebitTransactions() {
        Collection<Transaction> result;
        Logger.debug("Requested debit transactions of current user");
        try {
            result = transactionService.getUserDebitTransactions();
            Logger.info("Debit Transactions of user found");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions/credit")
    public ResponseEntity<Collection<Transaction>> getUserCreditTransactions() {
        Collection<Transaction> result;
        Logger.debug("Requested credit transactions of current user");
        try {
            result = transactionService.getUserCreditTransactions();
            Logger.info("Credit Transactions of user found");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> makeTransaction(@RequestParam(name = "recipientMail") final String recipientMail,
                                                  @RequestParam(name = "description") final String description,
                                                  @RequestParam(name = "amount") final double amount) {
        Logger.debug("Requested transaction creation between authenticated user and user with mail {}", recipientMail);
        try {
            transactionService.makeTransaction(recipientMail, description, amount);
            Logger.info("New transaction created between users.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        } catch (DataAccessException | ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
