package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    @GetMapping("/user/home")
    public User home() {
        UserCredentials principal = (UserCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }

    @GetMapping("/user/settings")
    public User settings() {
        UserCredentials principal = (UserCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }

    //TODO Ajouter la méthode PUT pour settings et les tests correspondants.
}
