package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@RestController
@RequestMapping(produces = "application/json")
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestParam(name = "username") final String username,
                                               @RequestParam(name = "mail") final String mail,
                                               @RequestParam(name = "password") final String password) {
        try {
            Logger.debug("POST request for user registration.");
            accountService.registrateUser(username, mail, password);
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<String>("Sorry, an error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Logger.info("User has successfully registered.");
        return new ResponseEntity<String>("Registration succeed", HttpStatus.OK);
    }
}
