package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.dtos.ContactUserDTO;
import com.paymybuddy.payapp.services.ContactService;
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
@DisplayName("Contact Controller tests on : ")
public class ContactControllerTest {
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    // Mocks
    private MockMvc mvc;
    @MockBean
    private ContactService contactService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("GET Contacts Success")
    public void Given_authenticatedUser_When_getUserContacts_Then_returnContactServiceValue() throws Exception {
        // Empty collection
        Collection<ContactUserDTO> contactUserDTOS = Collections.emptyList();
        when(contactService.getUserContacts()).thenReturn(contactUserDTOS);
        String contactsJson = objectMapper.writeValueAsString(contactUserDTOS);


        MvcResult result;
        result = mvc.perform(get("/contacts"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(contactsJson);

        // At least one element in collection
        contactUserDTOS = Collections.singletonList(new ContactUserDTO(4, "username2", "username2@mail.com"));
        when(contactService.getUserContacts()).thenReturn(contactUserDTOS);
        contactsJson = objectMapper.writeValueAsString(contactUserDTOS);

        result = mvc.perform(get("/contacts"))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(contactsJson);
    }

    @Test
    @WithMockUser
    @DisplayName("GET Contacts Exception")
    public void Given_databaseError_When_getUserContacts_Then_returnNull() throws Exception {
        // Empty collection
        doThrow(DataRetrievalFailureException.class).when(contactService).getUserContacts();

        MvcResult result;
        result = mvc.perform(get("/contacts"))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");

    }

    @Test
    @WithMockUser
    @DisplayName("POST Contact Success")
    public void Given_contactMail_When_addContact_Then_statusIsCreated() throws Exception {
        doNothing().when(contactService).addContact(anyString());
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(post("/contacts")
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
    @DisplayName("POST Contact Exception")
    public void Given_databaseError_When_addContact_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(contactService).addContact(anyString());
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(post("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE Contact Success")
    public void Given_contactMail_When_deleteContact_Then_statusIsOK() throws Exception {
        doNothing().when(contactService).deleteContact(anyString());
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(delete("/contacts")
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
    @DisplayName("DELETE Contact Exception")
    public void Given_databaseError_When_deleteContact_Then_statusIsServerError() throws Exception {
        doThrow(DataRetrievalFailureException.class).when(contactService).deleteContact(anyString());
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(delete("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        // Check that a String was passed to give some information
        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }
}
