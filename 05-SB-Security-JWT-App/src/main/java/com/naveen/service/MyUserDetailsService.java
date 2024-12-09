package com.naveen.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.naveen.entity.Customer;
import com.naveen.repo.CustomerRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepo customerRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Customer cUN = customerRepo.findByUname(username);
		return new User(cUN.getUname(), cUN.getPwd(), Collections.emptyList());
		
//		Customer customer = customerRepo.findByUname(username);
//
//        if (customer == null) {
//            throw new UsernameNotFoundException("User not found: " + username);
//        }
//
//        // Log the user retrieval process
//        System.out.println("User found: " + username);
//
//        // Include roles or authorities
//        List<GrantedAuthority> authorities = Collections.singletonList(
//            new SimpleGrantedAuthority(customer.getRole())
//        );
//
//        return new User(customer.getUname(), customer.getPwd(), authorities);
	}

}
