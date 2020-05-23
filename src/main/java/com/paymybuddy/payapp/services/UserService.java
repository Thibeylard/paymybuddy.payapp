package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public Optional<User> getUserById(final int userId);

    public Optional<User> getUserByMail(final String mail);

    public User updateUserSettings(User user);
}
