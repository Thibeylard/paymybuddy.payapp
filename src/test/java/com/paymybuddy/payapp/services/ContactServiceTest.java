package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.ContactDAO;
import com.paymybuddy.payapp.dtos.ContactUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ContactService tests on : ")
public class ContactServiceTest {
    // Beans
    @Autowired
    private ContactService contactService;
    @MockBean
    private ContactDAO contactDAO;

    @Test
    @WithMockUser
    @DisplayName("getContacts() Success")
    public void Given_authenticatedUser_When_getUserContacts_Then_returnContactDAOValue() {

        Collection<ContactUserDTO> contactsEmpty = Collections.emptyList();
        Collection<ContactUserDTO> contactUserDTOS = Collections.singletonList(new ContactUserDTO(2, "username2", "username2@mail.com"));
        when(contactDAO.getContactsByUserMail(anyString()))
                .thenReturn(contactsEmpty)
                .thenReturn(contactUserDTOS);

        // Empty collection
        assertThat(contactService.getUserContacts())
                .isEqualTo(contactsEmpty);

        // At least singleton list
        assertThat(contactService.getUserContacts())
                .isEqualTo(contactUserDTOS);
    }

    @Test
    @WithMockUser
    @DisplayName("getContacts() Exception")
    public void Given_databaseError_When_getUserContacts_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(contactDAO).getContactsByUserMail(anyString());

        assertThrows(DataRetrievalFailureException.class, () -> contactService.getUserContacts());

    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Success")
    public void Given_contactMail_When_addContact_Then_nothingIsThrown() {
        when(contactDAO.save(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> contactService.addContact("someuser@mail.com"));
        verify(contactDAO, times(1)).save(anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Exception")
    public void Given_databaseError_When_addContact_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(contactDAO).save(anyString(), anyString());

        assertThrows(DataRetrievalFailureException.class, () -> contactService.addContact("someuser@mail.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Success")
    public void Given_contactMail_When_deleteContact_Then_nothingIsThrown() {
        when(contactDAO.delete(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> contactService.deleteContact("someuser@mail.com"));
        verify(contactDAO, times(1)).delete(anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Exception")
    public void Given_databaseError_When_deleteContact_Then_throwsDAOException() {
        doThrow(DataRetrievalFailureException.class).when(contactDAO).delete(anyString(), anyString());

        assertThrows(DataRetrievalFailureException.class, () -> contactService.deleteContact("someuser@mail.com"));
    }
}
