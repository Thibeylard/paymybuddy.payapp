package com.paymybuddy.payapp.models;

import java.util.ArrayList;
import java.util.Collection;

public class BankAccount {
    /**
     * ID in database.
     */
    private final int id;
    /**
     * user_id of owner in database.
     */
    private final int ownerID;
    /**
     * All operations made on this bank account
     */
    private final Collection<BankOperation> operations;
    /**
     * Basic description of Bank Account.
     */
    private String description;
    /**
     * IBAN number of real bank account.
     */
    private String IBAN;

    public BankAccount(int id, int ownerID, String description, String IBAN) {
        this.id = id;
        this.ownerID = ownerID;
        this.description = description;
        this.IBAN = IBAN;
        this.operations = new ArrayList<>();
    }

    public BankAccount(int id, int ownerID, String description, String IBAN, Collection<BankOperation> operations) {
        this.id = id;
        this.ownerID = ownerID;
        this.description = description;
        this.IBAN = IBAN;
        this.operations = operations;
    }

    public int getId() {
        return id;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public Collection<BankOperation> getOperations() {
        return operations;
    }
}
