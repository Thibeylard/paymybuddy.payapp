package com.paymybuddy.payapp.models;

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
    private final int debtorId;
    /**
     * Database ID of User that received the money.
     */
    private final int creditorId;

    /**
     * Datetime of transaction
     */
    private final ZonedDateTime date;
    /**
     * Initial transaction amount.
     */
    private final Double initialAmount;
    /**
     * 5% app monetized total.
     */
    private final Double total;
    /**
     * Basic description of transaction
     */
    private final String description;

    /**
     * Constructor used to retrieve a transaction from database
     *
     * @param id            from Transaction.id
     * @param debtorId      from Transaction.debtor_id
     * @param creditorId    from Transaction.creditor_id
     * @param date          from Transaction.date
     * @param initialAmount from Transaction.initialTotal
     * @param total         from Transaction.monetizedTotal
     * @param description   from Transaction.description
     */
    public Transaction(int id,
                       int debtorId,
                       int creditorId,
                       ZonedDateTime date,
                       Double initialAmount,
                       Double total,
                       String description) {
        this.id = id;
        this.debtorId = debtorId;
        this.creditorId = creditorId;
        this.date = date;
        this.initialAmount = initialAmount;
        this.total = total;
        this.description = description;
    }


    // ----------------------------------- Attribute Getters and Setters

    public Optional<Integer> getId() {
        return Optional.ofNullable(this.id);
    }

    public int getDebtorId() {
        return debtorId;
    }

    public int getCreditorId() {
        return creditorId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Double getInitialAmount() {
        return initialAmount;
    }

    public Double getTotal() {
        return total;
    }

    public String getDescription() {
        return description;
    }
}
