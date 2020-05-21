package com.paymybuddy.payapp.services;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;

public interface AccountService {

    void registrateUser(@NotEmpty @Size(min = 5, max = 25) String username,
                        @Email String mail,
                        @Size(min = 8, max = 80) String password) throws SQLException, IllegalArgumentException;
}
