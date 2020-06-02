package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.models.Contact;
import com.paymybuddy.payapp.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
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
    public void Given_userId_When_getUserContacts_Then_returnContactServiceValue() throws Exception {
        // Empty collection
        Collection<Contact> contacts = Collections.emptyList();
        when(contactService.getContactsByUserId(anyInt())).thenReturn(contacts);
        String contactsJson = objectMapper.writeValueAsString(contacts);
        params.add("userID", "4");

        MvcResult result;
        result = mvc.perform(get("/contacts")
                .params(params))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(contactsJson);

        // At least one element in collection
        contacts = Collections.singletonList(new Contact(4, "username2", "username2@mail.com"));
        when(contactService.getContactsByUserId(anyInt())).thenReturn(contacts);
        contactsJson = objectMapper.writeValueAsString(contacts);

        result = mvc.perform(get("/contacts")
                .params(params))
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
        Collection<Contact> contacts = Collections.emptyList();
        doThrow(SQLException.class).when(contactService).getContactsByUserId(anyInt());
        params.add("userID", "4");

        MvcResult result;
        result = mvc.perform(get("/contacts")
                .params(params))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("");

    }

    @Test
    @WithMockUser
    @DisplayName("POST Contact Success")
    public void Given_userIdAndContactMail_When_addContact_Then_statusIsCreated() throws Exception {
        doNothing().when(contactService).addContact(anyInt(), anyString());
        params.add("userID", "4");
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(post("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isCreated())// Status CREATED
                .andReturn();

        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("POST Contact Exception")
    public void Given_databaseError_When_addContact_Then_statusIsServerError() throws Exception {
        doThrow(SQLException.class).when(contactService).addContact(anyInt(), anyString());
        params.add("userID", "4");
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(post("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE Contact Success")
    public void Given_userIdAndContactMail_When_deleteContact_Then_statusIsOK() throws Exception {
        doNothing().when(contactService).deleteContact(anyInt(), anyString());
        params.add("userID", "4");
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(delete("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isOk())// Status OK
                .andReturn();

        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE Contact Exception")
    public void Given_databaseError_When_deleteContact_Then_statusIsServerError() throws Exception {
        doThrow(SQLException.class).when(contactService).deleteContact(anyInt(), anyString());
        params.add("userID", "4");
        params.add("contactMail", "contact@mail.com");

        MvcResult result;
        result = mvc.perform(delete("/contacts")
                .params(params)
                .with(csrf()))
                .andExpect(status().isInternalServerError())// Status INTERNAL SERVER ERROR
                .andReturn();

        assertThat(result.getResponse().getContentAsString().isBlank())
                .isFalse();
    }
}
