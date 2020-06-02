package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.services.ContactService;
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

    @Test
    @DisplayName("GET Contacts")
    public void Given_userId_When_getUserContacts_Then_returnContactServiceValue() {

    }

    @Test
    @DisplayName("POST Contact")
    public void Given_userIdAndContactMail_When_addContact_Then_returnInfoOnOperation() {

    }

    @Test
    @DisplayName("DELETE Contact")
    public void Given_userIdAndContactMail_When_deleteContact_Then_returnInfoOnOperation() {

    }
}
