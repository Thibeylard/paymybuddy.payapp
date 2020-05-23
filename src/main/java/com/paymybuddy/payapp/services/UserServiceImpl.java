package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Service;

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
    public User updateUserSettings(User user) {
        return null;
    }
}
