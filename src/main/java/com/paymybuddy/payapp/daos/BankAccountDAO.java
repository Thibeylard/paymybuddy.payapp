package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface BankAccountDAO {

    /**
     * Get all bankAccounts of User from BankAccount table
     *
     * @return All User bankAccounts as BankAccount Collection
     * @throws DataAccessException if error occurs during database operation
     */
    Collection<BankAccount> getUserBankAccounts(final String userMail)
            throws DataAccessException;

    /**
     * Add new row to BankAccount table.
     *
     * @param userMail    mail of User to add bankAccount to
     * @param description description to set for BankAccount
     * @param IBAN        IBAN number to set for BankAccount
     * @throws DataAccessException if error occurs during database operation
     */
    boolean addBankAccount(final String userMail, final String description, final String IBAN)
            throws DataAccessException;

    /**
     * Update description or IBAN of BankAccount with ID bankAccountID in BankAccount table.
     *
     * @param bankAccountID ID of BankAccount to update
     * @param description   new description to set for BankAccount
     * @param IBAN          new IBAN number to set for BankAccount
     * @throws DataAccessException if error occurs during database operation
     */
    boolean updateBankAccount(final int bankAccountID, final String description, final String IBAN)
            throws DataAccessException;

    /**
     * Delete bankAccount with ID bankAccountID from BankAccount table.
     *
     * @param bankAccountID ID of BankAccount to delete
     * @throws DataAccessException if error occurs during database operation
     */
    boolean deleteBankAccount(final int bankAccountID)
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
     * Add new row in BankOperation table (amount with negative value).
     *
     * @param bankAccountID ID of BankAccount to operate on
     * @param amount        Amount of money to transfer
     */
    boolean transferMoney(final int bankAccountID, final int amount)
            throws DataAccessException;

    /**
     * Add new row in BankOperation table (amount with positive value).
     *
     * @param bankAccountID ID of BankAccount to operate on
     * @param amount        Amount of money to withdraw
     */
    boolean withdrawMoney(final int bankAccountID, final int amount)
            throws DataAccessException;
}
