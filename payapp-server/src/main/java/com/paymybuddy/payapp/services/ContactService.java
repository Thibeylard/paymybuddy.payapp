package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.dtos.ContactUserDTO;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface ContactService {

    /**
     * Get all contacts of authenticated User from Contact table
     *
     * @return All User contacts as User Collection
     * @throws DataAccessException if error occurs during database operation
     */
    Collection<ContactUserDTO> getUserContacts()
            throws DataAccessException;

    /**
     * Add contact between authenticated User and another User identified by contactMail.
     *
     * @param contactMail User mail
     * @throws DataAccessException if error occurs during database operation
     */
    void addContact(final String contactMail)
            throws DataAccessException;

    /**
     * Delete contact between authenticated User and another User identified by contactMail.
     *
     * @param contactMail User mail
     * @throws DataAccessException if error occurs during database operation
     */
    void deleteContact(final String contactMail)
            throws DataAccessException;
}
