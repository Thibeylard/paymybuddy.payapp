package com.paymybuddy.payapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("prod")
@DisplayName("User Stories tests : ")
public class UserStoriesIT {
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    // Mocks
    private MockMvc mvc;
    //Attributes
    private String baseUrl;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private MvcResult result;
    private String jsonExpected;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    @Test
    @FlywayTest
    @DisplayName("Story#1 : New User got blank account")
    public void newUserSignsInAndGotFreshAccount() throws Exception {
        String userPass = "fiftybucks";

        User user = new User("maurobertson",
                "maurice.robertson@example.com",
                bcryptEncoder.encode(userPass),
                Collections.singletonList(Role.USER));

        // ---------------------------------------------------------------- User signs in
        params.clear();
        params.add("username", user.getUsername());
        params.add("mail", user.getMail());
        params.add("password", userPass);
        mvc.perform(post("/registration")
                .params(params)
                .with(csrf())
                .with(anonymous()))
                .andExpect(status().isOk());

        // ---------------------------------------------------------------- User logs in successfully
        params.clear();
        mvc.perform(formLogin()
                .user(user.getMail())
                .password(userPass))
                .andExpect(authenticated());

        // ---------------------------------------------------------------- User has been properly saved
        result = mvc.perform(get("/user")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        User savedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getMail()).isEqualTo(savedUser.getMail());
        assertThat(user.getRoles()).usingRecursiveFieldByFieldElementComparator().isEqualTo(savedUser.getRoles());
        assertThat(bcryptEncoder.matches(userPass, user.getPassword())).isTrue();
        assertThat(bcryptEncoder.matches(userPass, savedUser.getPassword())).isTrue();

        // ---------------------------------------------------------------- User has no contacts
        result = mvc.perform(get("/contacts")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no transaction
        result = mvc.perform(get("/transactions")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has balance at 0
        result = mvc.perform(get("/user/balance")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(BigDecimal.valueOf(0.0));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no bill
        result = mvc.perform(get("/user/bills")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no bank account
        result = mvc.perform(get("/bankAccounts")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);
    }

    @Test
    @FlywayTest
    @DisplayName("Story#2 : Existing User ask new bill")
    public void existingUserLogsInAndAsksForBill() {
/*
        String userUsername = "maurice.robertson@example.com";

        // ---------------------------------------------------------------- User add three new contacts
        params.clear();
        params.add("contactMail", "nelson.harvey@example.com");
        mvc.perform(post("/contacts")
                .params(params)
                .with(csrf())
                .with(user(userUsername)))
                .andExpect(status().isCreated());

        params.clear();
        params.add("contactMail", "antonio.wright@example.com");
        mvc.perform(post("/contacts")
                .params(params)
                .with(csrf())
                .with(user(userUsername)))
                .andExpect(status().isCreated());

        params.clear();
        params.add("contactMail", "leslie.austin@example.com");
        mvc.perform(post("/contacts")
                .params(params)
                .with(csrf())
                .with(user(userUsername)))
                .andExpect(status().isCreated());

        userContacts = new ArrayList<>();
        userContacts.add(new ContactUserDTO(1, "nharvey", "nelson.harvey@example.com"));
        userContacts.add(new ContactUserDTO(2, "leslin", "leslie.austin@example.com"));
        userContacts.add(new ContactUserDTO(3, "antowright", "antonio.wright@example.com"));

        jsonExpected = objectMapper.writeValueAsString(userContacts);

        // ---------------------------------------------------------------- User has now three contacts
        params.clear();
        result = mvc.perform(get("/contacts")
                .params(params)
                .with(user(userUsername)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);*/
    }
}
