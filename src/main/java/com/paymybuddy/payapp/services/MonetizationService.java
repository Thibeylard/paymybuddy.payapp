package com.paymybuddy.payapp.services;

import java.math.BigDecimal;

public interface MonetizationService {
    BigDecimal monetize(final BigDecimal amount);
}
