package com.paymybuddy.payapp.services;

public interface AccountService {

    void registrateUser(String username, String mail, String password) throws IllegalArgumentException;
}
