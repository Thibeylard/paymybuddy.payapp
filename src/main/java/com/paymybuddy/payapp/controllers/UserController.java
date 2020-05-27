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

    @GetMapping("/")
    public String homeRedirection() {
        Logger.info("Redirection to home.");
        return "redirect:" + "/user/home";
    }

    @GetMapping("/user/home")
    public ResponseEntity<User> home() {
        Logger.debug("Request for user home.");
        return userInstanceResponse();
    }

    @GetMapping("/user/settings")
    public ResponseEntity<User> settings() {
        Logger.debug("Request for user settings.");
        return userInstanceResponse();
    }

    /**
     * Common method used for request that need to return Principal User instance.
     *
     * @return User or null in response body
     */
    private ResponseEntity<User> userInstanceResponse() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userService.getUserByMail(principal.getUsername());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/user/settings")
    public ResponseEntity<String> updateSettings(@RequestParam(name = "password") String password,
                                                 @RequestParam(name = "username") String usernameToSet,
                                                 @RequestParam(name = "mail") String mailToSet,
                                                 @RequestParam(name = "newPassword", required = false) String passwordToSet) {
        try {
            Logger.debug("Request to update user settings.");
            userService.updateSettings(password, usernameToSet, mailToSet, passwordToSet);
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
