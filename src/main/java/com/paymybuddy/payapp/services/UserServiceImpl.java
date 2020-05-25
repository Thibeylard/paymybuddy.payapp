package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
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
    public void updateSettings(String password,
                               String mail,
                               String username,
                               String newPassword)
            throws SQLException, ConstraintViolationException, BadCredentialsException {
    }
}
