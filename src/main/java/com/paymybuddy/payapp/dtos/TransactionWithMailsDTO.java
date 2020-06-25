package com.paymybuddy.payapp.dtos;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionWithMailsDTO {

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
     * Transaction amount.
     */
    private final BigDecimal amount;
    /**
     * Commission on transaction amount.
     */
    private final BigDecimal commission;
    /**
     * Basic description of transaction
     */
    private final String description;

    public TransactionWithMailsDTO(String userMail,
                                   String recipientMail,
                                   ZonedDateTime date,
                                   BigDecimal initialAmount,
                                   BigDecimal commission,
                                   String description) {
        this.userMail = userMail;
        this.recipientMail = recipientMail;
        this.date = date;
        this.amount = initialAmount;
        this.commission = commission;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public String getDescription() {
        return description;
    }
}
