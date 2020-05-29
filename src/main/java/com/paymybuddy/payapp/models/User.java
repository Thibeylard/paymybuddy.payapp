package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;


public class User {

    /**
     * ID in database
     */
    private final @NotNull int id;
    /**
     * User pseudonym
     */
    private @NotNull String username;
    /**
     * User mail and identifier (= username in Spring Security context)
     */
    private @NotNull @Email String mail;
    /**
     * User encrypted password
     */
    private @NotNull String password;
    /**
     * User roles defining app authorizations
     */
    private @NotNull Collection<Role> roles;
    /**
     * Other User instance connected to this User
     */
    private Collection<User> contacts;
    /**
     * User transactions
     */
    private Collection<Transaction> transactions;
    /**
     * User current balance
     */
    private Double balance;

    public User(int id) {
        this.id = id;
    }

    //TODO Le format de Building pour le constructeur n'est pas adapté en fin de compte.
    // Username, Mail, Password, et Roles au moins devraient être obligatoire.
    public User withUsername(String username) {
        this.username = username;
        return this;
    }

    public User withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public User withRoles(Collection<Role> roles) {
        this.roles = roles;
        return this;
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

    public int getId() {
        return id;
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

    public Collection<User> getContacts() {
        return contacts;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public Double getBalance() {
        return balance;
    }
}
