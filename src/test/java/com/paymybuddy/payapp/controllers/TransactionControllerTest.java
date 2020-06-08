package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.models.Transaction;
import com.paymybuddy.payapp.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Contact Controller tests on : ")
public class TransactionControllerTest {
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    // Mocks
    private MockMvc mvc;
    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
    }

    @Test
    @DisplayName("GET Transactions")
    @WithMockUser
    public void Given_authenticatedUser_When_getUserTransactions_Then_returnTransactionServiceValue() throws Exception {
        MvcResult result;
        ZonedDateTime transactionTime = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());

        // --------------------------------------------------------------------- Empty collection for all getTransactions methods
        Collection<Transaction> transactions = Collections.emptyList();
        String transactionsJson = objectMapper.writeValueAsString(transactions);

        // --------------------------------------------------------------------- All transactions
        when(transactionService.getUserTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);

        // --------------------------------------------------------------------- Debit transactions
        when(transactionService.getUserDebitTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions/debit"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);

        // --------------------------------------------------------------------- Credit transactions
        when(transactionService.getUserCreditTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions/credit"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);

        // --------------------------------------------------------------------- At least one element in collection for all getTransactions methods
        transactions = Collections.singletonList(new Transaction(2,
                4,
                transactionTime,
                10.00,
                9.50,
                "sample"));
        transactionsJson = objectMapper.writeValueAsString(transactions);

        // --------------------------------------------------------------------- All transactions
        when(transactionService.getUserTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);

        // --------------------------------------------------------------------- Debit transactions
        when(transactionService.getUserDebitTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions/debit"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);

        // --------------------------------------------------------------------- Credit transactions
        when(transactionService.getUserCreditTransactions()).thenReturn(transactions);

        result = mvc.perform(get("/transactions/credit"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(transactionsJson);
    }

    @Test
    @DisplayName("GET Transactions Exceptions")
    @WithMockUser
    public void Given_databaseError_When_getUserTransactions_Then_returnNull() throws Exception {
        // Empty collection
        Collection<Transaction> transactions = Collections.emptyList();
        MvcResult result;

        // --------------------------------------------------------------------- All transactions
        doThrow(DataRetrievalFailureException.class).when(transactionService).getUserTransactions();

        result = mvc.perform(get("/transactions"))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");

        // --------------------------------------------------------------------- Debit transactions
        doThrow(DataRetrievalFailureException.class).when(transactionService).getUserDebitTransactions();

        result = mvc.perform(get("/transactions/debit"))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");

        // --------------------------------------------------------------------- Credit transactions
        doThrow(DataRetrievalFailureException.class).when(transactionService).getUserCreditTransactions();

        result = mvc.perform(get("/transactions/credit"))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");
    }

    @Test
    @DisplayName("POST Transaction")
    @WithMockUser
    public void Given_validParams_When_addContact_Then_statusIsCreated() throws Exception {
        doNothing().when(transactionService).makeTransaction(anyString(), anyString(), anyDouble());
        params.add("recipientMail", "recipient@mail.com");
        params.add("description", "sample description");
        params.add("amount", "10.00");

        MvcResult result;
        result = mvc.perform(post("/transactions")
                .params(params)
                .with(csrf()))
                .andExpect(status().isCreated())// Status CREATED
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @DisplayName("POST Transaction Exceptions")
    @WithMockUser
    public void Given_databaseError_When_addContact_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(transactionService).makeTransaction(anyString(), anyString(), anyDouble());
        params.add("recipientMail", "recipient@mail.com");
        params.add("description", "sample description");
        params.add("amount", "10.00");

        MvcResult result;
        result = mvc.perform(post("/transactions")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }
}
