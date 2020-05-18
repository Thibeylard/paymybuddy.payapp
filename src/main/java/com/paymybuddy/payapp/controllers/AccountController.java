package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // TODO définir type retour
    /*
    @GetMapping("/login")
    public void login() {

    }

    // TODO définir type retour
    @GetMapping("/logout")
    public void logout() {

    }
    */
    @PostMapping("/registration")
    public ResponseEntity<Boolean> registration(@RequestParam(name = "username") final String username,
                                                @RequestParam(name = "mail") final String mail,
                                                @RequestParam(name = "password") final String password) {
        try {
            accountService.registrateUser(username, mail, password);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
