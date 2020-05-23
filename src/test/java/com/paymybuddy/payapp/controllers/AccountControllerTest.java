package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import com.paymybuddy.payapp.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Account Controller tests on : ")
public class AccountControllerTest {

    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    // Mocks
    private MockMvc mvc;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    @MockBean
    private AccountService mockAccountService;

    private String baseUrl;
    private UserDetails userDetails;
    @MockBean
    private UserDetailsService mockUserDetailsService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        userDetails = new UserCredentials(new User(14)
                .withUsername("user")
                .withMail("user@mail.com")
                .withPassword(bcryptEncoder.encode("userpass"))
                .withRoles(Collections.singletonList(Role.USER)));
    }

    @Test
    @DisplayName("Anonymous user redirections")
    public void Given_anonymousUser_When_requestAppAccess_Then_redirectedToLogin() throws Exception {
        mvc.perform(get("/user/home")
                .with(anonymous()))
                .andExpect(redirectedUrl(this.baseUrl + "/login"));

        mvc.perform(get("/user/settings")
                .with(anonymous()))
                .andExpect(redirectedUrl(this.baseUrl + "/login"));
    }

    @Test
    @DisplayName("User registration failed on 404")
    public void Given_anonymousUser_When_registratesWithUnavailableMail_Then_statusIsBadRequest() throws Exception {
        params.add("username", "user");
        params.add("mail", "user@mail.com");
        params.add("password", "pass");
        doThrow(IllegalArgumentException.class).when(mockAccountService).registrateUser(anyString(), anyString(), anyString());
        mvc.perform(post("/registration")
                .params(params)
                .with(csrf())
                .with(anonymous()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User registration failed on 405")
    public void Given_databaseError_When_userRegistrates_Then_statusIsInternalServerError() throws Exception {
        params.add("username", "user");
        params.add("mail", "user@mail.com");
        params.add("password", "pass");
        doThrow(SQLException.class).when(mockAccountService).registrateUser(anyString(), anyString(), anyString());
        mvc.perform(post("/registration")
                .params(params)
                .with(csrf())
                .with(anonymous()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("User registration succeed")
    public void Given_anonymousUser_When_registratesWithAvailableMail_Then_userRedirectedToLogin() throws Exception {
        params.add("username", "user");
        params.add("mail", "user@mail.com");
        params.add("password", "pass");
        doNothing().when(mockAccountService).registrateUser(anyString(), anyString(), anyString());
        mvc.perform(post("/registration")
                .params(params)
                .with(csrf())
                .with(anonymous()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User login failed")
    public void Given_anonymousUser_When_loginWithWrongCredentials_Then_isNotAuthenticated() throws Exception {
        when(mockUserDetailsService.loadUserByUsername(anyString())).thenThrow(UsernameNotFoundException.class);
        mvc.perform(formLogin()
                .user("wronguser")
                .password("wrongpassword"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("User login succeed")
    public void Given_anonymousUser_When_loginWithValidCredentials_Then_isAuthenticated() throws Exception {
        when(mockUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        mvc.perform(formLogin()
                .user("user@mail.com")
                .password("userpass"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("Authenticated user accesses")
    public void Given_authenticatedUser_When_requestAppAccess_Then_accessToRequestedPage() throws Exception {
        mvc.perform(get("/user/home")
                .with(user("user@mail.com").roles(Role.USER.name())))
                .andExpect(status().isOk());

        mvc.perform(get("/user/settings")
                .with(user("user@mail.com").roles(Role.USER.name())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User logout")
    public void Given_authenticatedUser_When_logout_Then_IsNoMoreAuthenticated() throws Exception {
        mvc.perform(logout())
                .andExpect(unauthenticated());
    }

}
