package com.paymybuddy.payapp.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MonetizationServiceImpl implements MonetizationService {

    private final BigDecimal percentage;

    public MonetizationServiceImpl() {
        this.percentage = BigDecimal.valueOf(0.005);
    }

    @Override
    public BigDecimal monetize(final BigDecimal amount) {
        return amount.multiply(percentage);
    }
}
