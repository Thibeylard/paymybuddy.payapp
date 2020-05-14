package com.paymybuddy.payapp.models;

public class User {
    private String username;
    private String email;
    private String password;
    private final Role[] roles;

    public User(String username, String email, String password, Role[] roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // ----------------------------------- GETTERS AND SETTERS
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
