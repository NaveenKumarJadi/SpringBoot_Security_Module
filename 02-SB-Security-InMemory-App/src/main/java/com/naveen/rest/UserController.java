package com.naveen.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to my world";
    }

    @GetMapping("/about")
    public String about() {
    	return "What will you do, If I tell about me!";
    }

    @GetMapping("/come")
//    @PreAuthorize("hasRole('BUJJI')") // Use hasRole for role-based access control
    public String Complex() {
    	return "Bujji... let's go!";
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasRole('USER')")  // Use hasRole for role-based access control
    public String userProfile() {
        return "Only user and admin have access into Complex!";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasRole('ADMIN')")  // Use hasRole for role-based access control
    public String adminProfile() {
        return "only admin have access of Complex";
    }
}