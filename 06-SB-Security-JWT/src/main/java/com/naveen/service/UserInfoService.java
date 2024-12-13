package com.naveen.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naveen.entity.UserInfo;
import com.naveen.repo.UserInfoRepository;

/*

The UserInfoService class is a service layer that integrates with Spring Security for user authentication and
interacts with the database to manage user information. 
It implements UserDetailsService, a core Spring Security interface, to load user-specific data during the authentication process.
 */
@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    // This method overrides loadUserByUsername from the UserDetailsService interface.
    // It calls loadUserByUsername to fetch user details from the database.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        // If the user exists, wraps the UserInfo object in the UserInfoDetails class, which implements UserDetails.
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    public String addUser(UserInfo userInfo) {
        // Encode password before saving the user
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }
}