package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.Contact;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(produces = "application/json")
public class ContactController {

    @GetMapping("/contacts")
    public ResponseEntity<Collection<Contact>> getUserContacts(@RequestParam(name = "userID") int userID) {
        return null;
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> addContact(@RequestParam(name = "userID") int userID,
                                             @RequestParam(name = "contactMail") String contactMail) {
        return null;
    }

    @DeleteMapping("/contacts")
    public ResponseEntity<String> deleteContact(@RequestParam(name = "userID") int userID,
                                                @RequestParam(name = "contactMail") String contactMail) {
        return null;
    }
}
