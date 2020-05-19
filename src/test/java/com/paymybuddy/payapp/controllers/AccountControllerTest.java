package com.paymybuddy.payapp.controllers;

import com.paymybuddy.payapp.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Account Controller tests on : ")
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AccountService mockAccountService;

    private MockMvc mvc;
    private String baseUrl;
    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
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
    @DisplayName("User registration failed")
    public void Given_anonymousUser_When_registratesWithUnavailableMail_Then_statusIsForbidden() throws Exception {
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
        mvc.perform(formLogin()
                .user("wronguser")
                .password("wrongpassword"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("User login succeed")
    public void Given_anonymousUser_When_loginWithValidCredentials_Then_isAuthenticated() throws Exception {
        mvc.perform(formLogin()
                .user("martin@gmail.com")
                .password("martinpass"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("Authenticated user accesses")
    public void Given_authenticatedUser_When_requestAppAccess_Then_accessToRequestedPage() throws Exception {
        mvc.perform(get("/user/home")
                .with(user("martin@gmail.com")))
                .andExpect(status().isOk());

        mvc.perform(get("/user/settings")
                .with(user("martin@gmail.com")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User logout")
    public void Given_authenticatedUser_When_logout_Then_IsNoMoreAuthenticated() throws Exception {
        mvc.perform(logout())
                .andExpect(unauthenticated());
    }

}
