package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class BankAccountController {

    private final ContactService contactService;

    @Autowired
    public BankAccountController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/bankAccounts")
    public ResponseEntity<Collection<BankAccount>> getUserBankAccounts() {
        return null;
    }

    @PostMapping("/bankAccounts")
    public ResponseEntity<String> addBankAccount(@RequestParam(name = "IBAN") final String IBAN,
                                                 @RequestParam(name = "description") final String description) {
        return null;
    }

    @PutMapping("/bankAccounts")
    public ResponseEntity<String> updateBankAccount(@RequestParam(name = "id") final int bankAccountID,
                                                    @RequestParam(name = "IBAN") final String IBAN,
                                                    @RequestParam(name = "description") final String description) {
        return null;
    }

    @DeleteMapping("/bankAccounts")
    public ResponseEntity<String> deleteBankAccount(@RequestParam(name = "id") final int bankAccountID) {
        return null;
    }


    @GetMapping("/bankAccount/operations")
    public ResponseEntity<Collection<BankAccount>> getUserBankOperations(@RequestParam(name = "id") final int bankAccountID) {
        return null;
    }

    @PostMapping("/bankAccount/transfer")
    public ResponseEntity<String> transferToBank(@RequestParam(name = "id") final int bankAccountID,
                                                 @RequestParam(name = "amount") final double amount) {
        return null;
    }

    @PostMapping("/bankAccount/withdraw")
    public ResponseEntity<String> withdrawFromBank(@RequestParam(name = "id") final int bankAccountID,
                                                   @RequestParam(name = "amount") final double amount) {
        return null;
    }
}
