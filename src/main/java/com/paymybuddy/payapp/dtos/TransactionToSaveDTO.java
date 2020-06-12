package com.paymybuddy.payapp.dtos;

import java.time.ZonedDateTime;

public class TransactionToSaveDTO {

    /**
     * Mail of User that send the money.
     */
    private final String userMail;
    /**
     * Mail of User that receives the money.
     */
    private final String recipientMail;

    /**
     * Datetime of transaction
     */
    private final ZonedDateTime date;
    /**
     * Initial transaction amount.
     */
    private final Double amount;
    /**
     * 5% app monetized total.
     */
    private final Double total;
    /**
     * Basic description of transaction
     */
    private final String description;

    public TransactionToSaveDTO(String userMail, String recipientMail, ZonedDateTime date, Double initialAmount, Double total, String description) {
        this.userMail = userMail;
        this.recipientMail = recipientMail;
        this.date = date;
        this.amount = initialAmount;
        this.total = total;
        this.description = description;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getRecipientMail() {
        return recipientMail;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getTotal() {
        return total;
    }

    public String getDescription() {
        return description;
    }
}
