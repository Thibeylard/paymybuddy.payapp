package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.exceptions.UnauthorizedBankOperationException;

import java.math.BigDecimal;

public interface BankService {
    /**
     * Ask for User Bank to perform withdrawal.
     *
     * @param ownerFullName Name of bank account owner
     * @param IBAN          IBAN of bank account
     * @param amount        Amount to withdraw
     * @return True if bank authorized operation
     * @throws UnauthorizedBankOperationException if bank refused operation
     */
    boolean askBankForWithdrawal(final String ownerFullName,
                                 final String IBAN,
                                 final BigDecimal amount) throws UnauthorizedBankOperationException;

    /**
     * Ask for User Bank to perform transfer.
     *
     * @param ownerFullName Name of bank account owner
     * @param IBAN          IBAN of bank account
     * @param amount        Amount to transfer
     * @return True if bank authorized operation
     * @throws UnauthorizedBankOperationException if bank refused operation
     */
    boolean askBankForTransfer(final String ownerFullName,
                               final String IBAN,
                               final BigDecimal amount) throws UnauthorizedBankOperationException;
}
