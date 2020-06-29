package com.paymybuddy.payapp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.paymybuddy.payapp.enums.Role;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@JsonPropertyOrder({"id", "username", "mail", "password", "roles"})
public class User {

    /**
     * ID in database. Null at in-app creation.
     */
    @Nullable
    private final Integer id;
    /**
     * User pseudonym
     */
    @NotNull
    @JsonProperty("username")
    private final String username;
    /**
     * User mail and identifier (= username in Spring Security context)
     */
    @NotNull
    @JsonProperty("mail")
    private final String mail;
    /**
     * User encrypted password
     */
    @NotNull
    @JsonProperty("password")
    private final String password;
    /**
     * User roles defining app authorizations
     */
    @NotNull
    @JsonProperty("roles")
    private final Collection<Role> roles;

    public User(@NotNull String username,
                @NotNull String mail,
                @NotNull String password,
                @NotNull Collection<Role> roles) {
        this.id = null;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.roles = roles;
    }

    @JsonCreator
    public User(
            @JsonProperty("id") @NotNull Integer id,
            @JsonProperty("username") @NotNull String username,
            @JsonProperty("mail") @NotNull String mail,
            @JsonProperty("password") @NotNull String password,
            @JsonProperty("roles") @NotNull Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
}
