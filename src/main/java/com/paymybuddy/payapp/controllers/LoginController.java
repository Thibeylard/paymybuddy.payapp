package com.paymybuddy.payapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class LoginController {

    // TODO define return type
    @GetMapping("/login")
    public void login() {

    }

    // TODO define return type
    @GetMapping("/logout")
    public void logout() {

    }
}
