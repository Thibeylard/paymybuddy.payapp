package com.paymybuddy.payapp.services;

import org.springframework.stereotype.Service;

@Service
public class MonetizationServiceImpl implements MonetizationService {

    private final double percentage;

    public MonetizationServiceImpl() {
        this.percentage = 0.05;
    }

    @Override
    public double monetize(final double amount) {
        return amount * percentage;
    }
}
