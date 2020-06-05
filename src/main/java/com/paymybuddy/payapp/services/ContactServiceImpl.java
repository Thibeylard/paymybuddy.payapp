package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.ContactDAO;
import com.paymybuddy.payapp.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    public Collection<Contact> getUserContacts() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return contactDAO.getContactsByUserMail(authUser.getUsername());
    }

    /**
     * @see ContactService
     */
    @Override
    public void addContact(String contactMail) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contactDAO.save(authUser.getUsername(), contactMail);
    }

    /**
     * @see ContactService
     */
    @Override
    public void deleteContact(String contactMail) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contactDAO.delete(authUser.getUsername(), contactMail);
    }
}
