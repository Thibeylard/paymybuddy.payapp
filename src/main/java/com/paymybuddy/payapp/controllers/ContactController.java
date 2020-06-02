package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Contact;
import com.paymybuddy.payapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    public ResponseEntity<Collection<Contact>> getUserContacts(@RequestParam(name = "userID") int userID) {
        Collection<Contact> result;
        Logger.debug("Requested contacts of user with id {}", userID);
        try {
            result = contactService.getContactsByUserId(userID);
            Logger.info("Contacts of user with id {} found", userID);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> addContact(@RequestParam(name = "userID") int userID,
                                             @RequestParam(name = "contactMail") String contactMail) {
        Logger.debug("Requested contact creation between user with id {} and user with mail {}", userID, contactMail);
        try {
            contactService.addContact(userID, contactMail);
            Logger.info("New contact created between users.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<String> deleteContact(@RequestParam(name = "userID") int userID,
                                                @RequestParam(name = "contactMail") String contactMail) {
        Logger.debug("Requested deletion of contact between user with id {} and user with mail {}", userID, contactMail);
        try {
            contactService.deleteContact(userID, contactMail);
            Logger.info("Specified contact successfully deleted.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (SQLException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
