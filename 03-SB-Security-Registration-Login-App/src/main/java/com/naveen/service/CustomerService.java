package com.naveen.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.naveen.entity.Customer;
import com.naveen.repository.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService {

	@Autowired
	CustomerRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Customer customer = repository.findByEmail(email);
		
		return new User(customer.getEmail(), customer.getPwd(), Collections.emptyList());
	}

}
