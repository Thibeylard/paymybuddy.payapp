package com.paymybuddy.payapp.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public class Transaction {

    /**
     * ID in database.
     */
    private final Integer id;
    /**
     * Database ID of User that send the money.
     */
    private final int debtorID;
    /**
     * Database ID of User that received the money.
     */
    private final int creditorID;

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

    /**
     * Constructor used to retrieve a transaction from database
     *
     * @param id          from Transaction.id
     * @param debtorID    from Transaction.debtor_id
     * @param creditorID  from Transaction.creditor_id
     * @param date        from Transaction.date
     * @param amount      from Transaction.initialTotal
     * @param commission  from Transaction.monetizedTotal
     * @param description from Transaction.description
     */
    public Transaction(int id,
                       int debtorID,
                       int creditorID,
                       ZonedDateTime date,
                       BigDecimal amount,
                       BigDecimal commission,
                       String description) {
        this.id = id;
        this.debtorID = debtorID;
        this.creditorID = creditorID;
        this.date = date;
        this.amount = amount;
        this.commission = commission;
        this.description = description;
    }


    // ----------------------------------- Attribute Getters and Setters

    public Optional<Integer> getId() {
        return Optional.ofNullable(this.id);
    }

    public int getDebtorID() {
        return debtorID;
    }

    public int getCreditorID() {
        return creditorID;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTotal() {
        return commission;
    }

    public String getDescription() {
        return description;
    }
}
