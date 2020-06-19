package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user") // Return basic view of current User (no Contacts, no Transactions)
    public ResponseEntity<User> getUser() {
        Logger.debug("Request for principal user.");
        Optional<User> user = userService.getUserByMail();
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/balance") // Return User balance
    public ResponseEntity<BigDecimal> getUserBalance() {
        Logger.debug("Request for principal user balance.");
        Optional<BigDecimal> balance = userService.getUserBalance();
        return balance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<String> updateUserProfile(@RequestParam(name = "password") final String password,
                                                    @RequestParam(name = "username") final String usernameToSet,
                                                    @RequestParam(name = "mail") final String mailToSet,
                                                    @RequestParam(name = "newPassword", required = false) final String passwordToSet) {
        try {
            Logger.debug("Request to update user settings.");
            userService.updateUserProfile(password, usernameToSet, mailToSet, passwordToSet);
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            Logger.error("Password does not match. Update aborted.");
            return new ResponseEntity<>("Incorrect password.", HttpStatus.FORBIDDEN);
        } catch (SQLException e) {
            Logger.error("A server error occurred : User could not be found.");
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Logger.info("Successfully update settings");
        return new ResponseEntity<>("Successfully update settings", HttpStatus.OK);
    }

}
