package com.naveen.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naveen.entity.Customer;
import com.naveen.repository.CustomerRepository;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody Customer c) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.getEmail(), c.getPwd());
		try {
			Authentication authenticate = authManager.authenticate(token);
			if (authenticate.isAuthenticated()) {
				return new ResponseEntity<String>("Welcome to ABC IT", HttpStatus.OK);
			}
		} catch (Exception e) {
			// logger
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registration(@RequestBody Customer c) {
		String encodedPwd = pwdEncoder.encode(c.getPwd());
		c.setPwd(encodedPwd);
		repo.save(c);
		return new ResponseEntity<String>("User Registered", HttpStatus.CREATED);
	}

}
