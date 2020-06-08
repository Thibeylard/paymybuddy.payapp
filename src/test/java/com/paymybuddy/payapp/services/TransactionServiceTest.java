package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.models.Transaction;
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
@DisplayName("TransactionService tests on : ")
public class TransactionServiceTest {
    // Beans
    @Autowired
    private TransactionService transactionService;
    @MockBean
    private TransactionDAO transactionDAO;

    @Test
    @WithMockUser
    @DisplayName("getTransactions() Success")
    public void Given_authenticatedUser_When_getUserTransactions_Then_returnTransactionDAOValue() {
        ZonedDateTime transactionTime = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        Collection<Transaction> transactionsEmpty = Collections.emptyList();
        Collection<Transaction> transactions = Collections.singletonList(new Transaction(2,
                4,
                transactionTime,
                10.00,
                9.50,
                "sample"));

        // --------------------------------------------------------------------- Empty collection for all getTransactions methods

        // --------------------------------------------------------------------- All transactions
        when(transactionDAO.getTransactionsByUserMail(anyString())).thenReturn(transactionsEmpty);
        assertThat(transactionService.getUserTransactions())
                .isEqualTo(transactionsEmpty);

        // --------------------------------------------------------------------- Debit transactions
        when(transactionDAO.getDebitTransactionsByUserMail(anyString())).thenReturn(transactionsEmpty);
        assertThat(transactionService.getUserDebitTransactions())
                .isEqualTo(transactionsEmpty);

        // --------------------------------------------------------------------- Credit transactions
        when(transactionDAO.getCreditTransactionsByUserMail(anyString())).thenReturn(transactionsEmpty);
        assertThat(transactionService.getUserCreditTransactions())
                .isEqualTo(transactionsEmpty);

        // --------------------------------------------------------------------- At least one element in collection for all getTransactions methods

        // --------------------------------------------------------------------- All transactions
        when(transactionDAO.getTransactionsByUserMail(anyString())).thenReturn(transactions);
        assertThat(transactionService.getUserTransactions())
                .isEqualTo(transactions);

        // --------------------------------------------------------------------- Debit transactions
        when(transactionDAO.getDebitTransactionsByUserMail(anyString())).thenReturn(transactions);
        assertThat(transactionService.getUserDebitTransactions())
                .isEqualTo(transactions);

        // --------------------------------------------------------------------- Credit transactions
        when(transactionDAO.getCreditTransactionsByUserMail(anyString())).thenReturn(transactions);
        assertThat(transactionService.getUserCreditTransactions())
                .isEqualTo(transactions);
    }

    @Test
    @WithMockUser
    @DisplayName("getTransactions() Exceptions")
    public void Given_databaseError_When_getUserContacts_Then_throwsDAOException() {
        // --------------------------------------------------------------------- All transactions
        doThrow(DataRetrievalFailureException.class).when(transactionDAO).getTransactionsByUserMail(anyString());
        assertThrows(DataRetrievalFailureException.class, () -> transactionService.getUserTransactions());

        // --------------------------------------------------------------------- Debit transactions
        doThrow(DataRetrievalFailureException.class).when(transactionDAO).getDebitTransactionsByUserMail(anyString());
        assertThrows(DataRetrievalFailureException.class, () -> transactionService.getUserDebitTransactions());

        // --------------------------------------------------------------------- Credit transactions
        doThrow(DataRetrievalFailureException.class).when(transactionDAO).getCreditTransactionsByUserMail(anyString());
        assertThrows(DataRetrievalFailureException.class, () -> transactionService.getUserCreditTransactions());

    }

    @Test
    @WithMockUser
    @DisplayName("makeTransaction() Success")
    public void Given_validParams_When_makeTransaction_Then_nothingIsThrown() throws Exception {
        when(transactionDAO.save(anyString(), anyString(), any(ZonedDateTime.class), anyString(), anyDouble(), anyDouble())).thenReturn(true);

        assertDoesNotThrow(() -> transactionService.makeTransaction("someuser@mail.com", "sampleDesription", 10.00));
        verify(transactionDAO, times(1)).save(anyString(), anyString(), any(ZonedDateTime.class), anyString(), anyDouble(), anyDouble());
    }

    @Test
    @WithMockUser
    @DisplayName("makeTransaction() Exceptions")
    public void Given_databaseError_When_makeTransaction_Then_throwsDAOException() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(transactionDAO).save(anyString(), anyString(), any(ZonedDateTime.class), anyString(), anyDouble(), anyDouble());
        assertThrows(DataRetrievalFailureException.class, () -> transactionService.makeTransaction("someuser@mail.com", "sampleDesription", 10.00));
    }
}
