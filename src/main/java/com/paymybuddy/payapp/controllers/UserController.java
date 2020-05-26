package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/home")
    public ResponseEntity<User> home() {
        return userInstanceResponse();
    }

    @GetMapping("/user/settings")
    public ResponseEntity<User> settings() {
        return userInstanceResponse();
    }

    private ResponseEntity<User> userInstanceResponse() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUserByMail(principal.getUsername());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/user/settings")
    public ResponseEntity<String> updateSettings(@RequestParam(name = "password") String password,
                                                 @RequestParam(name = "mail") String mail,
                                                 @RequestParam(name = "username") String username,
                                                 @RequestParam(name = "newPassword", required = false) String newPassword) {
        try {
            userService.updateSettings(password, mail, username, newPassword);
        } catch (ConstraintViolationException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            Logger.error("Password does not match. Update aborted.");
            return new ResponseEntity<>("Incorrect password.", HttpStatus.FORBIDDEN);
        } catch (SQLException e) {
            Logger.error("A server error occurred : User could not be found.");
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Successfully update settings", HttpStatus.OK);
    }
}
