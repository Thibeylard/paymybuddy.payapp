package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Optional;

@Service
public interface UserService {
    public Optional<User> getUserById(final int userId) throws SQLException;

    public Optional<User> getUserByMail(final String mail) throws SQLException;

    public Integer updateSettings(String password,
                                  String mail,
                                  String username,
                                  String newPassword) throws SQLException, ConstraintViolationException;
}
