package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Controller tests on : ")
public class UserControllerTest {
    private final MultiValueMap<String, String> paramsPUT = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    // Mocks
    private MockMvc mvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        paramsPUT.add("id", "6");
        paramsPUT.add("password", "somePass");
        paramsPUT.add("username", "someUsername");
        paramsPUT.add("mail", "someMail@mail.com");
        paramsPUT.add("newPassword", null);
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("GET on User pages succeed")
    public void Given_authenticatedUser_When_getAppURLs_Then_returnUserSpecificInstance() throws Exception {
        User user = new User("user",
                "user@mail.com",
                bcryptEncoder.encode("userpass"),
                Collections.singleton(Role.USER));
        String userJson = objectMapper.writeValueAsString(user);

        when(userService.getUserByMail(anyString()))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.of(user));

        MvcResult result;

        result = mvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(userJson);

        result = mvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(userJson);
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile succeed")
    public void Given_authenticatedUser_When_updateSettings_Then_returnUserID() throws Exception {
        doNothing().when(userService).updateSettings(anyString(), anyString(), anyString(), nullable(String.class));
        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile wrong values")
    public void Given_constraintEntryViolation_When_updateSettings_Then_statusIsBadRequest() throws Exception {
        doThrow(ConstraintViolationException.class).when(userService)
                .updateSettings(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile password does not match")
    public void Given_notMatchingPasswords_When_updateSettings_Then_statusIsForbidden() throws Exception {

        doThrow(BadCredentialsException.class).when(userService)
                .updateSettings(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile server error")
    public void Given_constraintEntryViolation_When_updateSettings_Then_statusIsInternalServerError() throws Exception {
        doThrow(SQLException.class).when(userService)
                .updateSettings(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}
