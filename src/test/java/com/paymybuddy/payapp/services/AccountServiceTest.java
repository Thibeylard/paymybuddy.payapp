package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Account Service tests on : ")
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private UserDAO mockUserDAO;

    @Test(expected = IllegalArgumentException.class)
    public void Given_usernameTooShort_When_registrates_Then_throwIllegalArgumentException() {
        accountService.registrateUser("a", "hello@mail.com", "longEnoughPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Given_usernameTooLong_When_registrates_Then_throwIllegalArgumentException() {
        accountService.registrateUser("VeryLongUsernameThatCannotBeSavedInDatabase", "hello@mail.com", "longEnoughPassword");
    }


    @Test(expected = IllegalArgumentException.class)
    public void Given_invalidMail_When_registrates_Then_throwIllegalArgumentException() {
        accountService.registrateUser("ValidUsername", "hello@mail", "longEnoughPassword");
    }


    @Test(expected = IllegalArgumentException.class)
    public void Given_invalidPassword_When_registrates_Then_throwIllegalArgumentException() {
        accountService.registrateUser("ValidUsername", "hello@mail.com", "pass");
    }

    @Test
    public void Given_validParams_When_registrates_Then_noIllegalArgumentExceptionThrown() {
        accountService.registrateUser("ValidUsername", "hello@mail.com", "longEnoughPassword");
    }
}
