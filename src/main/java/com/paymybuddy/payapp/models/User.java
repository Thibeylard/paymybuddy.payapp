package com.paymybuddy.payapp.models;

import com.paymybuddy.payapp.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class User implements UserDetails {

    private @NotNull int id;
    private @NotNull String username;
    private @NotNull @Email String mail;
    private @NotNull String password;
    private Role[] roles;

    public User(int id) {
        this.id = id;
    }

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

    public User withRoles(Role[] roles) {
        this.roles = roles;
        return this;
    }

    // ----------------------------------- FROM UserDetails Interface

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
}
