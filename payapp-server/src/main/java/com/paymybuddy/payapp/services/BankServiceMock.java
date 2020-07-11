package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.exceptions.UnauthorizedBankOperationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankServiceMock implements BankService {

    /**
     * @see BankService
     */
    @Override
    public boolean askBankForWithdrawal(String ownerFullName, String IBAN, BigDecimal amount) throws UnauthorizedBankOperationException {
        return true;
    }

    /**
     * @see BankService
     */
    @Override
    public boolean askBankForTransfer(String ownerFullName, String IBAN, BigDecimal amount) throws UnauthorizedBankOperationException {
        return true;
    }
}
