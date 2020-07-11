package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.dtos.ContactUserDTO;
import com.paymybuddy.payapp.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

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
    public ResponseEntity<Collection<ContactUserDTO>> getUserContacts() {
        Collection<ContactUserDTO> result;
        Logger.debug("Requested contacts of current user");
        try {
            result = contactService.getUserContacts();
            Logger.info("Contacts of current user found");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> addContact(@RequestParam(name = "contactMail") final String contactMail) {
        Logger.debug("Requested contact creation between authenticated user and user with mail {}", contactMail);
        try {
            contactService.addContact(contactMail);
            Logger.info("New contact created between users.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<String> deleteContact(@RequestParam(name = "contactMail") final String contactMail) {
        Logger.debug("Requested deletion of contact between authenticated user and user with mail {}", contactMail);
        try {
            contactService.deleteContact(contactMail);
            Logger.info("Specified contact successfully deleted.");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } catch (DataAccessException e) {
            Logger.error(e.getMessage());
            return new ResponseEntity<>("ERROR : See logs for further details.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
