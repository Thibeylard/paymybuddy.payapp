package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import com.paymybuddy.payapp.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/bankAccounts")
    public ResponseEntity<Collection<BankAccount>> getUserBankAccounts() {
        Collection<BankAccount> result;
        Logger.debug("Requested bankAccounts of current user");
        try {
            result = bankAccountService.getUserBankAccounts();
            Logger.info("BankAccounts retrieved");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bankAccounts")
    public ResponseEntity<String> addBankAccount(@RequestParam(name = "ownerFullName") final String ownerFullName,
                                                 @RequestParam(name = "description") final String description,
                                                 @RequestParam(name = "IBAN") final String IBAN) {
        Logger.debug("Requested bankAccount creation with IBAN {}", IBAN);
        try {
            bankAccountService.addBankAccount(ownerFullName, description, IBAN);
            Logger.info("New bankAccount created.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/bankAccounts")
    public ResponseEntity<String> updateBankAccount(@RequestParam(name = "bankAccountID") final int bankAccountID,
                                                    @RequestParam(name = "ownerFullName") final String ownerFullName,
                                                    @RequestParam(name = "description") final String description,
                                                    @RequestParam(name = "IBAN") final String IBAN) {
        Logger.debug("Requested bankAccount {} to be updated with IBAN {}", bankAccountID, IBAN);
        try {
            bankAccountService.updateBankAccount(bankAccountID, ownerFullName, description, IBAN);
            Logger.info("BankAccount successfully modified.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/bankAccounts")
    public ResponseEntity<String> deleteBankAccount(@RequestParam(name = "bankAccountID") final int bankAccountID) {
        Logger.debug("Requested bankAccount {} to be deleted", bankAccountID);
        try {
            bankAccountService.deleteBankAccount(bankAccountID);
            Logger.info("BankAccount successfully deleted.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/bankAccount/operations")
    public ResponseEntity<Collection<BankOperation>> getUserBankOperations(@RequestParam(name = "bankAccountID") final int bankAccountID) {
        Collection<BankOperation> result;
        Logger.debug("Requested bankOperations of current user's specified bankAccount");
        try {
            result = bankAccountService.getBankAccountOperations(bankAccountID);
            Logger.info("BankOperations retrieved");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO Dans le cas de transfer et withdraw, une autre exception peut avoir lieu : Celle de l'erreur liée à la banque elle même
    // à répercuter dans Service aussi.
    @PostMapping("/bankAccount/transfer")
    public ResponseEntity<String> transferToBank(@RequestParam(name = "bankAccountID") final int bankAccountID,
                                                 @RequestParam(name = "amount") final double amount) {
        Logger.debug("Requested to make transfer operation from bankAccount {} ", bankAccountID);
        try {
            bankAccountService.transferMoney(bankAccountID, new BigDecimal(amount));
            Logger.info("Transfer succeed.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bankAccount/withdraw")
    public ResponseEntity<String> withdrawFromBank(@RequestParam(name = "bankAccountID") final int bankAccountID,
                                                   @RequestParam(name = "amount") final double amount) {
        Logger.debug("Requested to make withdraw operation to bankAccount {} ", bankAccountID);
        try {
            bankAccountService.withdrawMoney(bankAccountID, new BigDecimal(amount));
            Logger.info("Withdraw succeed.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
