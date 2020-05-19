package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO {

    Optional<User> findById(final int id);

    Optional<User> findByMail(final String mail);

    Optional<User> findByUsername(final String username);

    boolean saveUser(final String username, final String mail, final String password) throws IllegalArgumentException;
}
