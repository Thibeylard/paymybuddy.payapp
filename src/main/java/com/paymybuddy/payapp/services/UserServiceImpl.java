package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return userDAO.findById(userId);
    }

    @Override
    public Optional<User> getUserByMail(String mail) {
        return userDAO.findByMail(mail);
    }

    @Override
    public void updateSettings(final int id,
                               final String password,
                               final @Email String mail,
                               final @NotEmpty @Size(min = 5, max = 25) String username,
                               @Nullable @Size(min = 8, max = 80) String newPassword)
            throws SQLException, ConstraintViolationException, BadCredentialsException {

        String principalPassword = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (!passwordEncoder.matches(password, principalPassword)) {
            throw new BadCredentialsException("");
        }

        if (newPassword != null) {
            newPassword = passwordEncoder.encode(newPassword);
        }

        userDAO.updateSettings(id, mail, username, newPassword);
    }
}
