package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;
import java.util.Collections;

@Service
@Validated
public class AccountServiceImpl implements AccountService {


    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, UserDAO userDAO, @Qualifier("defaultValidator") Validator validator) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see AccountService
     */
    @Override
    public void registrateUser(final @NotEmpty @Size(min = 5, max = 25) String username,
                               final @Email String mail,
                               @Size(min = 8, max = 80) String password) throws SQLException, IllegalArgumentException, ConstraintViolationException {
        Logger.debug("Encode user password.");
        password = passwordEncoder.encode(password);
        userDAO.save(new User(username, mail, password, Collections.singletonList(Role.USER)));
    }
}
