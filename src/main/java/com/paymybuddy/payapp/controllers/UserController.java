package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    @GetMapping("/home")
    public User home() {
        return null;
    }

    @GetMapping("/settings")
    public User settings() {
        return null;
    }
}
