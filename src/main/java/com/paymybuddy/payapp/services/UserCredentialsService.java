package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredentialsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserCredentialsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Optional<User> user = userDAO.find(mail);
        return new UserCredentials(user.orElseThrow(() -> new UsernameNotFoundException(mail)));
    }
}
