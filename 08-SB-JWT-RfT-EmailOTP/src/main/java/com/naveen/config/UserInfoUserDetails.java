package com.naveen.config;

import java.util.Arrays;
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

public class UserInfoUserDetails implements UserDetails {


    private String name; // Changed from 'name' to 'username' for clarity
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(UserInfo userInfo) {
        name=userInfo.getName(); // Assuming 'name' is used as 'username'
        password=userInfo.getPassword();
        authorities= Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Converts the user's roles or permissions into a collection of GrantedAuthority objects required by Spring Security.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Return the user's password and username, respectively, from the UserInfo entity.
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    // These return true to indicate the account's active status (can be modified based on application requirements).
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
