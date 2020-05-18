package com.paymybuddy.payapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private String baseUrl;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    @Test
    public void Given_AnonymousUser_When_RequestAppAccess_Then_RedirectedToLogin() throws Exception {
        mvc.perform(get("/home").with(anonymous()))
                .andExpect(redirectedUrl(this.baseUrl + "/login"));

        mvc.perform(get("/settings").with(anonymous()))
                .andExpect(redirectedUrl(this.baseUrl + "/login"));
    }


}
