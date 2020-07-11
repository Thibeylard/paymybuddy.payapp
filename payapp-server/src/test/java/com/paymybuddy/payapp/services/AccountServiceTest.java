package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Account Service tests on : ")
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private UserDAO mockUserDAO;

    @Test
    @DisplayName("Username too short")
    public void Given_usernameTooShort_When_registrates_Then_throwConstraintViolationException() throws ConstraintViolationException {
        assertThrows(ConstraintViolationException.class, () -> accountService.registrateUser("a", "hello@mail.com", "longEnoughPassword"));
    }

    @Test
    @DisplayName("Username too long")
    public void Given_usernameTooLong_When_registrates_Then_throwConstraintViolationException() throws ConstraintViolationException {
        assertThrows(ConstraintViolationException.class, () -> accountService.registrateUser("VeryLongUsernameThatCannotBeSavedInDatabase", "hello@mail.com", "longEnoughPassword"));
    }


    @Test
    @DisplayName("Invalid mail")
    public void Given_invalidMail_When_registrates_Then_throwConstraintViolationException() throws ConstraintViolationException {
        assertThrows(ConstraintViolationException.class, () -> accountService.registrateUser("ValidUsername", "hellomail", "longEnoughPassword"));
    }


    @Test
    @DisplayName("Password too short")
    public void Given_invalidPassword_When_registrates_Then_throwConstraintViolationException() throws ConstraintViolationException {
        assertThrows(ConstraintViolationException.class, () -> accountService.registrateUser("ValidUsername", "hello@mail.com", "pass"));
    }

    @Test
    @DisplayName("Valid registration")
    public void Given_validParams_When_registrates_Then_noConstraintViolationExceptionThrown() throws SQLException {
        accountService.registrateUser("ValidUsername", "hello@mail.com", "longEnoughPassword");
    }
}
