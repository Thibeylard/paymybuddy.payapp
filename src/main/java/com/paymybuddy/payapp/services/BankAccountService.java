package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;

public interface BankAccountService {

    /**
     * Get all bankAccounts of authenticated User from BankAccount table
     *
     * @return All bankAccounts as BankAccount Collection
     * @throws DataAccessException if error occurs during database operation
     */
    Collection<BankAccount> getUserBankAccounts()
            throws DataAccessException;

    /**
     * Add new BankAccount to authenticated User.
     *
     * @param description   description to set for BankAccount
     * @param ownerFullName new ownerFullName to set for BankAccount
     * @param IBAN          IBAN number to set for BankAccount
     * @throws DataAccessException if error occurs during database operation
     */
    void addBankAccount(final String ownerFullName,
                        @Size(min = 10, max = 30) final String description,
                        final String IBAN)
            throws DataAccessException;

    /**
     * Update description or IBAN of BankAccount with ID bankAccountID
     *
     * @param bankAccountID ID of BankAccount to update
     * @param ownerFullName new ownerFullName to set for BankAccount
     * @param description   new description to set for BankAccount
     * @param IBAN          new IBAN number to set for BankAccount
     * @throws DataAccessException if error occurs during database operation
     */
    void updateBankAccount(final int bankAccountID,
                           final String ownerFullName,
                           @Size(min = 10, max = 30) final String description,
                           final String IBAN)
            throws DataAccessException;

    /**
     * Delete bankAccount with ID bankAccountID.
     *
     * @param bankAccountID ID of BankAccount to delete
     * @throws DataAccessException if error occurs during database operation
     */
    void deleteBankAccount(final int bankAccountID)
            throws DataAccessException;

    /**
     * Get all operations of specified bankAccount.
     *
     * @param bankAccountID ID of BankAccount to get operations from
     * @return All operations as BankOperation collection
     * @throws DataAccessException if error occurs during database operation
     */
    Collection<BankOperation> getBankAccountOperations(final int bankAccountID)
            throws DataAccessException;

    /**
     * Transfer money from application to real bank account.
     *
     * @param bankAccountID ID of BankAccount to operate on
     * @param amount        Amount of money to transfer
     */
    void transferMoney(final int bankAccountID, final BigDecimal amount)
            throws DataAccessException;

    /**
     * Withdraw money from real bank account to application.
     *
     * @param bankAccountID ID of BankAccount to operate on
     * @param amount        Amount of money to withdraw
     */
    void withdrawMoney(final int bankAccountID, final BigDecimal amount)
            throws DataAccessException;
}
