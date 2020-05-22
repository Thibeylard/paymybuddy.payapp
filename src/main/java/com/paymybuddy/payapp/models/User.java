package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;


public class User {

    private final @NotNull int id;
    private @NotNull String username;
    private @NotNull @Email String mail;
    private @NotNull String password;
    private ArrayList<Role> roles;

    public User(int id) {
        this.id = id;
    }

    public User withUsername(String username) {
        this.username = username;
        return this;
    }

    public User withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public User withRoles(ArrayList<Role> roles) {
        this.roles = roles;
        return this;
    }


    // ----------------------------------- Attribute Getters and Setters

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }
}
