package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    @GetMapping("/user/home")
    public ResponseEntity<User> home() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO Implémenter UserService pour aller récupérer l'utilisateur correspondant à celui du SecurityContext
        return new ResponseEntity<>(new User(2), HttpStatus.OK);
    }

    @GetMapping("/user/settings")
    public ResponseEntity<User> settings() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO Implémenter UserService pour aller récupérer l'utilisateur correspondant à celui du SecurityContext
        return new ResponseEntity<>(new User(2), HttpStatus.OK);
    }

    //TODO Ajouter la méthode PUT pour settings et les tests correspondants.
}
