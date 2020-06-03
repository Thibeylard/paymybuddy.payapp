package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.ContactDAO;
import com.paymybuddy.payapp.models.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ContactService tests on : ")
public class ContactServiceTest {
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private ContactService contactService;
    @MockBean
    private ContactDAO contactDAO;

    @Test
    @WithMockUser
    @DisplayName("getContacts() Success")
    public void Given_userId_When_getUserContacts_Then_returnContactDAOValue() throws Exception {

        Collection<Contact> contactsEmpty = Collections.emptyList();
        Collection<Contact> contacts = Collections.singletonList(new Contact(4, "username2", "username2@mail.com"));
        when(contactDAO.getContactsByUserId(anyInt()))
                .thenReturn(contactsEmpty)
                .thenReturn(contacts);

        // Empty collection
        assertThat(contactService.getContactsByUserId(4))
                .isEqualTo(contactsEmpty);

        // At least singleton list
        assertThat(contactService.getContactsByUserId(4))
                .isEqualTo(contacts);
    }

    @Test
    @WithMockUser
    @DisplayName("getContacts() Exception")
    public void Given_databaseError_When_getUserContacts_Then_throwsSameException() throws Exception {
        doThrow(SQLException.class).when(contactDAO).getContactsByUserId(anyInt());

        assertThrows(SQLException.class, () -> contactService.getContactsByUserId(4));

    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Success")
    public void Given_userIdAndContactMail_When_addContact_Then_nothingIsThrown() throws Exception {
        when(contactDAO.save(anyInt(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> contactService.addContact(4, "someuser@mail.com"));
        verify(contactDAO, times(1)).save(anyInt(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Exception")
    public void Given_databaseError_When_addContact_Then_throwsSameException() throws Exception {
        doThrow(SQLException.class).when(contactDAO).save(anyInt(), anyString());

        assertThrows(SQLException.class, () -> contactService.addContact(4, "someuser@mail.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Success")
    public void Given_userIdAndContactMail_When_deleteContact_Then_nothingIsThrown() throws Exception {
        when(contactDAO.delete(anyInt(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> contactService.deleteContact(4, "someuser@mail.com"));
        verify(contactDAO, times(1)).delete(anyInt(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Exception")
    public void Given_databaseError_When_deleteContact_Then_throwsSameException() throws Exception {
        doThrow(SQLException.class).when(contactDAO).delete(anyInt(), anyString());

        assertThrows(SQLException.class, () -> contactService.deleteContact(4, "someuser@mail.com"));
    }
}
