package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Contact;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Collection;

@Repository
public class ContactDAOImpl implements ContactDAO {

    /**
     * @see ContactDAO
     */
    @Override
    public Collection<Contact> getContactsByUserId(int userId) throws SQLException {
        return null;
    }

    /**
     * @see ContactDAO
     */
    @Override
    public boolean save(int userId, String contactMail) throws SQLException {
        return false;
    }

    /**
     * @see ContactDAO
     */
    @Override
    public boolean delete(int userId, String contactMail) throws SQLException {
        return false;
    }
}
