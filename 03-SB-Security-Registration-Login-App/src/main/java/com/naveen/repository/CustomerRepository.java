package com.naveen.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naveen.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Serializable> {

	public Customer findByEmail(String email);
	
}
