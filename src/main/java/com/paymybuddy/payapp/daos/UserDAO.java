package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public interface UserDAO {

    User findById(int id) throws SQLException;

    User findByMail(String mail) throws SQLException;

    User findByUsername(String username);
}
