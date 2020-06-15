package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public class BankAccountServiceImpl implements BankAccountService {
    /**
     * @see BankAccountService
     */
    @Override
    public Collection<BankAccount> getUserBankAccounts() throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void addBankAccount(final String description, final String IBAN) throws DataAccessException {

    }

    /**
     * @see BankAccountService
     */
    @Override
    public void updateBankAccount(final int bankAccountID, final String description, final String IBAN) throws DataAccessException {

    }

    /**
     * @see BankAccountService
     */
    @Override
    public void deleteBankAccount(final int bankAccountID) throws DataAccessException {

    }

    /**
     * @see BankAccountService
     */
    @Override
    public Collection<BankOperation> getBankAccountOperations(int bankAccountID) throws DataAccessException {
        return null;
    }


    /**
     * @see BankAccountService
     */
    @Override
    public void transferMoney(final int bankAccountID, final int amount) throws DataAccessException {

    }

    /**
     * @see BankAccountService
     */
    @Override
    public void withdrawMoney(final int bankAccountID, final int amount) throws DataAccessException {

    }
}
