package com.paymybuddy.payapp.models;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class BankOperation {

    /**
     * ID in database.
     */
    private final Integer id;
    /**
     * Database ID of BankAccount related to operation.
     */
    private final int bankAccountID;

    /**
     * Datetime of operation
     */
    private final ZonedDateTime date;
    /**
     * Operation amount. Negative = transfer, positive = withdrawal.
     */
    private final BigDecimal amount;

    @JsonCreator
    public BankOperation(Integer id, int bankAccountID, ZonedDateTime date, BigDecimal amount) {
        this.id = id;
        this.bankAccountID = bankAccountID;
        this.date = date;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public int getBankAccountID() {
        return bankAccountID;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
