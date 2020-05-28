package com.paymybuddy.payapp.services;

public interface TransactionService {
    boolean sendMoney(String creditorUsername, String description, double amount);
}
