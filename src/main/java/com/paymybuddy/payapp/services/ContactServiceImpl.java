package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.ContactDAO;
import com.paymybuddy.payapp.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public Collection<Contact> getUserContacts() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return contactDAO.getContactsByUserMail(authUser.getUsername());
    }

    /**
     * @see ContactService
     */
    @Override
    public void addContact(final String contactMail) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contactDAO.save(authUser.getUsername(), contactMail);
    }

    /**
     * @see ContactService
     */
    @Override
    public void deleteContact(final String contactMail) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contactDAO.delete(authUser.getUsername(), contactMail);
    }
}
