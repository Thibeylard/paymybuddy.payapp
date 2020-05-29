package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


public class User {

    /**
     * ID in database. Null at in-app creation.
     */
    private final @Nullable
    Integer id;
    /**
     * User pseudonym
     */
    private final @NotNull String username;
    /**
     * User mail and identifier (= username in Spring Security context)
     */
    private final @NotNull String mail;
    /**
     * User encrypted password
     */
    private final @NotNull String password;
    /**
     * User roles defining app authorizations
     */
    private final @NotNull Collection<Role> roles;
    /**
     * Other User instance connected to this User. Null if User partially retrieved.
     */
    private @Nullable
    Collection<User> contacts;
    /**
     * User transactions.  Null if User partially retrieved.
     */
    private @Nullable
    Collection<Transaction> transactions;

    public User(@NotNull String username,
                @NotNull String mail,
                @NotNull String password,
                @NotNull Collection<Role> roles) {
        this.id = null;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.roles = roles;
        this.contacts = new ArrayList<User>();
        this.transactions = new ArrayList<Transaction>();
    }

    public User(int id, @NotNull String username, @NotNull String mail, @NotNull String password, @NotNull Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.roles = roles;
        this.contacts = null;
        this.transactions = null;
    }

    public User withContacts(Collection<User> contacts) {
        this.contacts = contacts;
        return this;
    }

    public User withTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }


    // ----------------------------------- Attribute Getters and Setters

    public Optional<Integer> getId() {
        return Optional.ofNullable(id);
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public Optional<Collection<User>> getContacts() {
        return Optional.ofNullable(contacts);
    }

    public Optional<Collection<Transaction>> getTransactions() {
        return Optional.ofNullable(transactions);
    }
}
