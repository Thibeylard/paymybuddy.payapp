package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.Contact;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;

@Service
public class ContactServiceImpl implements ContactService {
    /**
     * @see ContactService
     */
    @Override
    public Collection<Contact> getContactsByUserId(int userId) throws SQLException {
        return null;
    }

    /**
     * @see ContactService
     */
    @Override
    public void addContact(int userId, String contactMail) throws SQLException {

    }

    /**
     * @see ContactService
     */
    @Override
    public void deleteContact(int userId, String contactMail) throws SQLException {

    }
}
