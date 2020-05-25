package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Optional<User> getUserById(int userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByMail(String mail) {
        return Optional.empty();
    }

    @Override
    public Integer updateSettings(String password,
                                  String mail,
                                  String username,
                                  String newPassword)
            throws SQLException, ConstraintViolationException, BadCredentialsException {
        return null;
    }
}
