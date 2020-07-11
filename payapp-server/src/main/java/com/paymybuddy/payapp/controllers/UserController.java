package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Bill;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    private final UserService userService;
    private final String ZONE_ID;

    @Autowired
    public UserController(UserService userService, @Value("${default.zoneID}") String ZONE_ID) {
        this.userService = userService;
        this.ZONE_ID = ZONE_ID;
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

    @GetMapping("/user/bills") // Return User bills
    public ResponseEntity<Collection<Bill>> getUserBills() {
        Logger.debug("Request for principal user bills.");
        Collection<Bill> bills = userService.getUserBills();
        if (bills == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            Logger.info("Bills of user found.");
            return new ResponseEntity<>(bills, HttpStatus.OK);
        }
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

    @PostMapping("/user/newBill")
    public ResponseEntity<Bill> createUserBill(@RequestParam(name = "startDateYear") final int startDateYear,
                                               @RequestParam(name = "startDateMonth") final int startDateMonth,
                                               @RequestParam(name = "startDateDay") final int startDateDay,
                                               @RequestParam(name = "endDateYear") final int endDateYear,
                                               @RequestParam(name = "endDateMonth") final int endDateMonth,
                                               @RequestParam(name = "endDateDay") final int endDateDay) {
        ZonedDateTime startDate = ZonedDateTime.of(
                startDateYear,
                startDateMonth,
                startDateDay,
                0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime endDate = ZonedDateTime.of(
                endDateYear,
                endDateMonth,
                endDateDay,
                23, 59, 59, 999999999, ZoneId.of(ZONE_ID));
        Bill result;
        try {
            Logger.debug("Request to create new bill for user.");
            result = userService.createBill(startDate, endDate);
        } catch (IllegalArgumentException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            Logger.error("An unexpected error occurred : User could not be find.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Logger.info("Successfully created user bill");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
