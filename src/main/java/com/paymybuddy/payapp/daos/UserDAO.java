package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface UserDAO {

    Optional<User> findById(final int id);

    Optional<User> findByMail(final String mail);

    Optional<User> findByUsername(final String username);

    boolean save(final String username, final String mail, final String password) throws IllegalArgumentException, SQLException;

    boolean updateSettings(final String username, final String mail, final String newPassword) throws SQLException;
}
