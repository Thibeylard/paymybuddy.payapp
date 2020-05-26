package com.paymybuddy.payapp.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserCredentials implements UserDetails {

    private final String username; // User Mail, and not User username
    private final String password; // Encoded password
    private final Collection<? extends GrantedAuthority> roles;


    public UserCredentials(User user) {
        this.username = user.getMail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    public UserCredentials(String mail, String password, Collection<? extends GrantedAuthority> roles) {
        this.username = mail;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
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
}
