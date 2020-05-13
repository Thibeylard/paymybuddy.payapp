package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String password;
    private ArrayList<Role> roles;

    public User(String username, String email, String password, ArrayList<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
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

    public ArrayList<Role> getRoles() {
        return roles;
    }
}
