package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import com.paymybuddy.payapp.services.BankAccountService;
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

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("BankAccount Controller tests on : ")
public class BankAccountControllerTest {
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    // Mocks
    private MockMvc mvc;
    @MockBean
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("GET BankAccount Success")
    public void Given_authenticatedUser_When_getUserBankAccounts_Then_returnBankAccountServiceValue() throws Exception {
        // Empty collection
        Collection<BankAccount> bankAccounts = Collections.emptyList();
        when(bankAccountService.getUserBankAccounts()).thenReturn(bankAccounts);
        String accountsJson = objectMapper.writeValueAsString(bankAccounts);

        MvcResult result;
        result = mvc.perform(get("/bankAccounts")
                .params(params))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(accountsJson);

        // At least one element in collection
        bankAccounts = Collections.singletonList(new BankAccount(1, 2, "John SMITH", "some bank account", "FR76415724785412"));
        when(bankAccountService.getUserBankAccounts()).thenReturn(bankAccounts);
        accountsJson = objectMapper.writeValueAsString(bankAccounts);

        result = mvc.perform(get("/bankAccounts"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(accountsJson);
    }

    @Test
    @WithMockUser
    @DisplayName("GET BankAccount Exception")
    public void Given_databaseError_When_getUserBankAccounts_Then_returnNull() throws Exception {
        // Empty collection
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).getUserBankAccounts();

        MvcResult result;
        result = mvc.perform(get("/bankAccounts"))
                .andExpect(status().isInternalServerError())// Status INTERNAL_SERVER_ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");
    }

    @Test
    @WithMockUser
    @DisplayName("POST BankAccount Success")
    public void Given_validParams_When_addBankAccount_Then_statusIsCreated() throws Exception {
        doNothing().when(bankAccountService).addBankAccount(anyString(), anyString(), anyString());
        params.add("ownerFullName", "John SMITH");
        params.add("IBAN", "FR76415724785412");
        params.add("description", "some bank account");

        MvcResult result;
        result = mvc.perform(post("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isCreated())// Status CREATED
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("POST BankAccount Exception")
    public void Given_databaseError_When_addBankAccount_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).addBankAccount(anyString(), anyString(), anyString());
        params.add("ownerFullName", "John SMITH");
        params.add("IBAN", "FR76415724785412");
        params.add("description", "some bank account");

        MvcResult result;
        result = mvc.perform(post("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL_SERVER_ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();

        doThrow(ConstraintViolationException.class).when(bankAccountService).addBankAccount(anyString(), anyString(), anyString());

        params.set("description", "account");
        result = mvc.perform(post("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isBadRequest())// Status BAD REQUEST
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("PUT BankAccount Success")
    public void Given_validParams_When_updateBankAccount_Then_statusIsOK() throws Exception {
        doNothing().when(bankAccountService).updateBankAccount(anyInt(), anyString(), anyString(), anyString());
        params.add("bankAccountID", "4");
        params.add("ownerFullName", "John SMITH");
        params.add("IBAN", "FR76415724785412");
        params.add("description", "some bank account");

        MvcResult result;
        result = mvc.perform(put("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("PUT BankAccount Exception")
    public void Given_databaseError_When_updateBankAccount_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).updateBankAccount(anyInt(), anyString(), anyString(), anyString());
        params.add("bankAccountID", "4");
        params.add("ownerFullName", "John SMITH");
        params.add("IBAN", "FR76415724785412");
        params.add("description", "some bank account");

        MvcResult result;
        result = mvc.perform(put("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status is INTERNAL_SERVER_ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();

        doThrow(ConstraintViolationException.class).when(bankAccountService).updateBankAccount(anyInt(), anyString(), anyString(), anyString());

        params.set("description", "account");
        result = mvc.perform(put("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isBadRequest())// Status BAD REQUEST
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE BankAccount Success")
    public void Given_bankAccountId_When_deleteBankAccount_Then_statusIsOK() throws Exception {
        doNothing().when(bankAccountService).deleteBankAccount(anyInt());
        params.add("bankAccountID", "4");

        MvcResult result;
        result = mvc.perform(delete("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();

    }

    @Test
    @WithMockUser
    @DisplayName("DELETE BankAccount Exception")
    public void Given_databaseError_When_deleteBankAccount_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).deleteBankAccount(anyInt());
        params.add("bankAccountID", "4");

        MvcResult result;
        result = mvc.perform(delete("/bankAccounts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status is INTERNAL_SERVER_ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("GET BankOperations Success")
    public void Given_bankAccountId_When_getBankOperations_Then_returnBankAccountServiceValue() throws Exception {
        // Empty collection
        Collection<BankOperation> bankOps = Collections.emptyList();
        when(bankAccountService.getBankAccountOperations(anyInt())).thenReturn(bankOps);
        String opsJson = objectMapper.writeValueAsString(bankOps);
        params.add("bankAccountID", "1");

        MvcResult result;
        result = mvc.perform(get("/bankAccount/operations")
                .params(params))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(opsJson);

        // At least one element in collection
        bankOps = Collections.singletonList(new BankOperation(4,
                1,
                ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Paris")),
                new BigDecimal(100)));
        when(bankAccountService.getBankAccountOperations(anyInt())).thenReturn(bankOps);
        opsJson = objectMapper.writeValueAsString(bankOps);

        result = mvc.perform(get("/bankAccount/operations")
                .params(params))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(opsJson);
    }

    @Test
    @WithMockUser
    @DisplayName("GET BankOperations Exception")
    public void Given_databaseError_When_getBankOperations_Then_returnNull() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).getBankAccountOperations(anyInt());
        params.add("bankAccountID", "1");

        MvcResult result;
        result = mvc.perform(get("/bankAccount/operations")
                .params(params))
                .andExpect(status().isInternalServerError())// Status is INTERNAL_SERVER_ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");
    }

    @Test
    @WithMockUser
    @DisplayName("POST Transfer Success")
    public void Given_validParams_When_makeTransfer_Then_statusIsCreated() throws Exception {
        doNothing().when(bankAccountService).transferMoney(anyInt(), any(BigDecimal.class));
        params.add("bankAccountID", "1");
        params.add("amount", "150");

        MvcResult result;
        result = mvc.perform(post("/bankAccount/transfer")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk()) // Status OK
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("POST Transfer Exception")
    public void Given_databaseError_When_makeTransfer_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).transferMoney(anyInt(), any(BigDecimal.class));
        params.add("bankAccountID", "1");
        params.add("amount", "150");

        MvcResult result;
        result = mvc.perform(post("/bankAccount/transfer")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL_SERVER_ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();

        doThrow(ConstraintViolationException.class).when(bankAccountService).transferMoney(anyInt(), any(BigDecimal.class));

        params.set("amount", "-45");
        result = mvc.perform(post("/bankAccount/transfer")
                .params(params)
                .with(csrf()))
                .andExpect(status().isBadRequest())// Status BAD REQUEST
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("POST Withdraw Success")
    public void Given_validParams_When_makeWithdraw_Then_statusIsCreated() throws Exception {
        doNothing().when(bankAccountService).withdrawMoney(anyInt(), any(BigDecimal.class));
        params.add("bankAccountID", "1");
        params.add("amount", "150");

        MvcResult result;
        result = mvc.perform(post("/bankAccount/withdraw")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk()) // Status OK
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("POST Withdraw Exception")
    public void Given_databaseError_When_makeWithdraw_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(bankAccountService).withdrawMoney(anyInt(), any(BigDecimal.class));
        params.add("bankAccountID", "1");
        params.add("amount", "150");

        MvcResult result;
        result = mvc.perform(post("/bankAccount/withdraw")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL_SERVER_ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();

        doThrow(ConstraintViolationException.class).when(bankAccountService).withdrawMoney(anyInt(), any(BigDecimal.class));

        params.set("amount", "-45");
        result = mvc.perform(post("/bankAccount/withdraw")
                .params(params)
                .with(csrf()))
                .andExpect(status().isBadRequest())// Status BAD REQUEST
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }
}