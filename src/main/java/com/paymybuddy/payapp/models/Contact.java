package com.paymybuddy.payapp.models;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;


public class Contact {

    /**
     * ID in database. Not null because retrieved from database.
     */
    @NotNull
    private final Integer id;
    /**
     * Contact pseudonym
     */
    @NotNull
    private final String username;
    /**
     * Contact mail
     */
    @NotNull
    private final String mail;

    public Contact(@NotNull Integer id,
                   @NotNull String username,
                   @NotNull String mail) {
        this.id = id;
        this.username = username;
        this.mail = mail;
    }

    public Contact(@NotNull User user) {
        this.id = user.getId().orElseThrow(NoSuchElementException::new);
        this.username = user.getUsername();
        this.mail = user.getMail();
    }


    // ----------------------------------- Attribute Getters and Setters

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }
}
