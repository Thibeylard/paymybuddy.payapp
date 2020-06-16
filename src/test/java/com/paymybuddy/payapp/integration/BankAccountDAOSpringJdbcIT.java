package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.BankAccountDAO;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("TransactionDAO tests on : ")
public class BankAccountDAOSpringJdbcIT {
    @Autowired
    private BankAccountDAO bankAccountDAO;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("getBankAccounts() Successes")
    public void Given_userMail_When_getBankAccounts_Then_returnBankAccounts() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("save() Success")
    public void Given_validData_When_saveBankAccount_Then_tableTransactionUpdated() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("save() Exception")
    public void Given_databaseError_When_saveBankAccount_Then_throwException() {
    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("update() Success")
    public void Given_validData_When_updateBankAccount_Then_tableTransactionUpdated() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("update() Exception")
    public void Given_databaseError_When_updateBankAccount_Then_throwException() {
    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("delete() Success")
    public void Given_validData_When_deleteBankAccount_Then_tableTransactionUpdated() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("delete() Exception")
    public void Given_databaseError_When_deleteBankAccount_Then_throwException() {
    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("getBankOperations() Successes")
    public void Given_bankAccountID_When_getBankOperations_Then_returnBankOperations() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("saveTransfer() Success")
    public void Given_validData_When_saveTransfer_Then_tableTransactionUpdated() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("saveTransfer() Exception")
    public void Given_databaseError_When_saveTransfer_Then_throwException() {
    }


    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("saveWithdraw() Success")
    public void Given_authenticatedUser_When_saveWithdraw_Then_tableTransactionUpdated() {

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("saveWithdraw() Exception")
    public void Given_databaseError_When_saveWithdraw_Then_throwException() {
    }
}
