package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserCredentialsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserCredentialsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userDAO.findByMail(mail);
        } catch (SQLException e) {
            throw new UsernameNotFoundException(mail);
        }
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }

        return new UserCredentials(user);
    }
}
