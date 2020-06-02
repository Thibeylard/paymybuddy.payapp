package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.services.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

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

    @Test
    @DisplayName("GET Transactions")
    public void Given_userId_When_getUserTransactions_Then_returnTransactionServiceValue() {

    }

    @Test
    @DisplayName("POST Transaction")
    public void Given_validParams_When_addContact_Then_returnInfoOnOperation() {

    }
}
