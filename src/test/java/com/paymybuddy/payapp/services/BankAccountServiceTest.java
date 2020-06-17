package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.BankAccountDAO;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ContactService tests on : ")
public class BankAccountServiceTest {
    // Beans
    @Autowired
    private BankAccountService bankAccountService;
    @MockBean
    private BankAccountDAO bankAccountDAO;

    @Test
    @WithMockUser
    @DisplayName("getBankAccounts() Success")
    public void Given_authenticatedUser_When_getUserBankAccounts_Then_returnBankAccountDAOValue() {

        Collection<BankAccount> accountsEmpty = Collections.emptyList();
        Collection<BankAccount> accounts = Collections.singletonList(new BankAccount(1, 2, "John SMITH", "some bank account", "GB90RJCM65823550244646"));
        when(bankAccountDAO.getBankAccounts(anyString()))
                .thenReturn(accountsEmpty)
                .thenReturn(accounts);

        // Empty collection
        assertThat(bankAccountService.getUserBankAccounts())
                .isEqualTo(accountsEmpty);

        // At least singleton list
        assertThat(bankAccountService.getUserBankAccounts())
                .isEqualTo(accounts);
    }

    @Test
    @WithMockUser
    @DisplayName("getBankAccounts() Exception")
    public void Given_databaseError_When_getUserBankAccounts_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).getBankAccounts(anyString());

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.getUserBankAccounts());

    }

    @Test
    @WithMockUser
    @DisplayName("addBankAccount() Success")
    public void Given_validParams_When_addBankAccount_Then_nothingIsThrown() {
        when(bankAccountDAO.save(anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> bankAccountService.addBankAccount("John SMITH", "some description", "GB90RJCM65823550244646"));
        verify(bankAccountDAO, times(1)).save(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("addBankAccount() Exception")
    public void Given_databaseError_When_addBankAccount_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).save(anyString(), anyString(), anyString(), anyString());

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.addBankAccount("John SMITH", "some description", "GB90RJCM65823550244646"));
    }

    @Test
    @WithMockUser
    @DisplayName("updateBankAccount() Success")
    public void Given_validParams_When_updateBankAccount_Then_nothingIsThrown() {
        when(bankAccountDAO.update(anyInt(), anyString(), anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> bankAccountService.updateBankAccount(6, "John SMITH", "some description", "GB90RJCM65823550244646"));
        verify(bankAccountDAO, times(1)).update(anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("updateBankAccount() Exception")
    public void Given_databaseError_When_updateBankAccount_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).update(anyInt(), anyString(), anyString(), anyString());

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.updateBankAccount(6, "John SMITH", "some description", "GB90RJCM65823550244646"));
    }

    @Test
    @WithMockUser
    @DisplayName("deleteBankAccount() Success")
    public void Given_bankAccountID_When_deleteBankAccount_Then_nothingIsThrown() {
        when(bankAccountDAO.delete(anyInt())).thenReturn(true);

        assertDoesNotThrow(() -> bankAccountService.deleteBankAccount(3));
        verify(bankAccountDAO, times(1)).delete(anyInt());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteBankAccount() Exception")
    public void Given_databaseError_When_deleteBankAccount_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).delete(anyInt());

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.deleteBankAccount(3));
    }

    @Test
    @WithMockUser
    @DisplayName("getBankOperations() Success")
    public void Given_bankAccountID_When_getUserBankOperations_Then_returnBankAccountDAOValue() {
        Collection<BankOperation> opsEmpty = Collections.emptyList();
        Collection<BankOperation> ops = Collections.singletonList(new BankOperation(
                4,
                2,
                ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Paris")),
                new BigDecimal(100)));

        when(bankAccountDAO.getBankOperations(anyInt()))
                .thenReturn(opsEmpty)
                .thenReturn(ops);

        // Empty collection
        assertThat(bankAccountService.getBankAccountOperations(2))
                .isEqualTo(opsEmpty);

        // At least singleton list
        assertThat(bankAccountService.getBankAccountOperations(2))
                .isEqualTo(ops);
    }

    @Test
    @WithMockUser
    @DisplayName("getBankOperations() Exception")
    public void Given_databaseError_When_getUserBankOperations_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).getBankOperations(anyInt());

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.getBankAccountOperations(2));

    }

    @Test
    @WithMockUser
    @DisplayName("transferMoney() Success")
    public void Given_validParams_When_transferMoney_Then_nothingIsThrown() {
        when(bankAccountDAO.saveTransferOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class))).thenReturn(true);

        assertDoesNotThrow(() -> bankAccountService.transferMoney(6, new BigDecimal(45)));
        verify(bankAccountDAO, times(1)).saveTransferOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class));
    }

    @Test
    @WithMockUser
    @DisplayName("transferMoney() Exception")
    public void Given_databaseError_When_transferMoney_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).saveTransferOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class));

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.transferMoney(6, new BigDecimal(45)));
    }

    @Test
    @WithMockUser
    @DisplayName("withdrawMoney() Success")
    public void Given_validParams_When_withdrawMoney_Then_nothingIsThrown() {
        when(bankAccountDAO.saveWithdrawOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class))).thenReturn(true);

        assertDoesNotThrow(() -> bankAccountService.withdrawMoney(6, new BigDecimal(45)));
        verify(bankAccountDAO, times(1)).saveWithdrawOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class));
    }

    @Test
    @WithMockUser
    @DisplayName("withdrawMoney() Exception")
    public void Given_databaseError_When_withdrawMoney_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(bankAccountDAO).saveWithdrawOperation(anyInt(), any(ZonedDateTime.class), any(BigDecimal.class));

        assertThrows(DataRetrievalFailureException.class, () -> bankAccountService.withdrawMoney(6, new BigDecimal(45)));
    }
}
