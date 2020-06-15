package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public class BankAccountDAOSpringJdbc implements BankAccountDAO {
    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankAccount> getUserBankAccounts(String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean addBankAccount(String userMail, String description, String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean updateBankAccount(int bankAccountID, String description, String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean deleteBankAccount(int bankAccountID) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankOperation> getBankAccountOperations(int bankAccountID) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean transferMoney(int bankAccountID, int amount) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean withdrawMoney(int bankAccountID, int amount) throws DataAccessException {
        return false;
    }
}
