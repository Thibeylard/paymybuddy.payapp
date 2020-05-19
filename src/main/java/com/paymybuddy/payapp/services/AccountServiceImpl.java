package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, UserDAO userDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    @Override
    public void registrateUser(@NotEmpty @Size(min = 5, max = 25) String username,
                               @Email String mail,
                               @Size(min = 8) String password) throws IllegalArgumentException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<String>> violationSet = validator.validate(username);
        if (!violationSet.isEmpty())
            throw new IllegalArgumentException("Username must be between 5 and 25 characters.");

        violationSet = validator.validate(mail);
        if (!violationSet.isEmpty())
            throw new IllegalArgumentException("Mail has invalid format.");

        violationSet = validator.validate(password);
        if (!violationSet.isEmpty())
            throw new IllegalArgumentException("Password must have at least 8 characters.");

        password = passwordEncoder.encode(password);
        userDAO.saveUser(username, mail, password);
    }
}
