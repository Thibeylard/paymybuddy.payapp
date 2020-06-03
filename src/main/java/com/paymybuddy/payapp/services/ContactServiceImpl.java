package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.ContactDAO;
import com.paymybuddy.payapp.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDAO contactDAO;

    @Autowired
    public ContactServiceImpl(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    /**
     * @see ContactService
     */
    @Override
    public Collection<Contact> getContactsByUserId(int userId) throws SQLException {
        return contactDAO.getContactsByUserId(userId);
    }

    /**
     * @see ContactService
     */
    @Override
    public void addContact(int userId, String contactMail) throws SQLException {
        contactDAO.save(userId, contactMail);
    }

    /**
     * @see ContactService
     */
    @Override
    public void deleteContact(int userId, String contactMail) throws SQLException {
        contactDAO.delete(userId, contactMail);
    }
}
