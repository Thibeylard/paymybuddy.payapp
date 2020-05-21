package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;

@Service
@Validated
public class AccountServiceImpl implements AccountService {


    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, UserDAO userDAO, Validator validator) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registrateUser(@NotEmpty @Size(min = 5, max = 25) String username,
                               @Email String mail,
                               @Size(min = 8, max = 80) String password) throws SQLException, IllegalArgumentException {
        password = passwordEncoder.encode(password);
        userDAO.saveUser(username, mail, password);
    }
}
