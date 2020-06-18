package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.BankAccountDAO;
import com.paymybuddy.payapp.exceptions.UnauthorizedBankOperationException;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDAO bankAccountDAO;
    private final ClockService clockService;
    private final BankService bankService;

    @Autowired
    public BankAccountServiceImpl(BankAccountDAO bankAccountDAO) {
        this.bankAccountDAO = bankAccountDAO;
        this.clockService = new ClockService() {
        };
        this.bankService = new BankService() {
            @Override
            public boolean askBankForWithdrawal(String ownerFullName, String IBAN, BigDecimal amount) throws UnauthorizedBankOperationException {
                return true;
            }

            @Override
            public boolean askBankForTransfer(String ownerFullName, String IBAN, BigDecimal amount) throws UnauthorizedBankOperationException {
                return true;
            }
        };
    }

    /**
     * @see BankAccountService
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<BankAccount> getUserBankAccounts() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bankAccountDAO.getBankAccounts(authUser.getUsername());
    }

    /**
     * @see BankAccountService
     */
    @Override
    @Validated
    public void addBankAccount(final String ownerFullName,
                               @Size(min = 10, max = 30, message = "Description must be between 10 and 30 characters.") final String description,
                               final String IBAN) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bankAccountDAO.save(authUser.getUsername(), ownerFullName, description, IBAN);
    }

    /**
     * @see BankAccountService
     */
    @Override
    @Validated
    public void updateBankAccount(final int bankAccountID,
                                  final String ownerFullName,
                                  @Size(min = 10, max = 30, message = "Description must be between 10 and 30 characters.") final String description,
                                  final String IBAN) throws DataAccessException {
        bankAccountDAO.update(bankAccountID, ownerFullName, description, IBAN);
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void deleteBankAccount(final int bankAccountID) throws DataAccessException {
        bankAccountDAO.delete(bankAccountID);
    }

    /**
     * @see BankAccountService
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<BankOperation> getBankAccountOperations(int bankAccountID) throws DataAccessException {
        return bankAccountDAO.getBankOperations(bankAccountID);
    }


    /**
     * @see BankAccountService
     */
    @Override
    @Validated
    public void transferMoney(final int bankAccountID,
                              @Min(value = 0, message = "Bank operation can't be negative.") final BigDecimal amount)
            throws UnauthorizedBankOperationException, DataAccessException {
        BankAccount bankAccount = bankAccountDAO.getBankAccount(bankAccountID);
        if (bankService.askBankForTransfer(bankAccount.getOwnerFullName(), bankAccount.getIBAN(), amount)) {
            bankAccountDAO.saveTransferOperation(bankAccountID, clockService.now(), amount);
        }
    }

    /**
     * @see BankAccountService
     */
    @Override
    @Validated
    public void withdrawMoney(final int bankAccountID,
                              @Min(value = 0, message = "Bank operation can't be negative.") final BigDecimal amount)
            throws UnauthorizedBankOperationException, DataAccessException {
        BankAccount bankAccount = bankAccountDAO.getBankAccount(bankAccountID);
        if (bankService.askBankForWithdrawal(bankAccount.getOwnerFullName(), bankAccount.getIBAN(), amount)) {
            bankAccountDAO.saveWithdrawOperation(bankAccountID, clockService.now(), amount);
        }
    }
}
