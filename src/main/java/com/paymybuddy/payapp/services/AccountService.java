package com.paymybuddy.payapp.services;

import java.sql.SQLException;

public interface AccountService {

    void registrateUser(String username, String mail, String password) throws SQLException, IllegalArgumentException;
}
