package com.naveen.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.naveen.entity.UserInfo;

/*
 The UserInfoDetails class is a custom implementation of the UserDetails interface from Spring Security. 
 It provides Spring Security with the necessary user information for authentication and authorization.
 UserDetails is the standard interface used by Spring Security to retrieve user information during authentication
 */
public class UserInfoDetails implements UserDetails {

    private String username; // Changed from 'name' to 'username' for clarity
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername(); // Assuming 'name' is used as 'username'
        this.password = userInfo.getPassword();
        this.authorities = List.of(userInfo.getRoles().split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /* Converts the user's roles or permissions into a collection of GrantedAuthority objects required by Spring Security. */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /* Return the user's password and username, respectively, from the UserInfo entity. */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /* These return true to indicate the account's active status (can be modified based on application requirements). */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement your logic if you need this
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement your logic if you need this
    }
}
