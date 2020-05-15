package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;

public class User {
    private final int id;
    private final Role[] roles;
    private String username;
    private String email;
    private String password;

    public User(int id, String username, String email, String password, Role[] roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // ----------------------------------- GETTERS AND SETTERS

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
