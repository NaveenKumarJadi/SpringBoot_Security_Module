package com.naveen.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naveen.dto.AuthRequest;
import com.naveen.dto.AuthResponse;
import com.naveen.entity.Customer;
import com.naveen.repository.CustomerRepository;
import com.naveen.service.CustomerService;
import com.naveen.service.JwtService;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private JwtService jwtService;

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome to ABC IT";
	}

	@PostMapping("/register")
	public String registerCustomer(@RequestBody Customer customer) {

		// duplicate check
		log.info("Entering into registerCustomer() method : ");

		String encodedPwd = passwordEncoder.encode(customer.getPwd());
		customer.setPwd(encodedPwd);

		customerRepository.save(customer);

		log.info("Exiting registerCustomer() method : ");
		return "User registered";
	}
	
	/*
	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody Customer c) {

		log.info("Entering into loginCheck() method : ");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.getUname(), c.getPwd());

		try {
			Authentication authenticate = authenticationManager.authenticate(token);

			if (authenticate.isAuthenticated()) {
				String jwtToken = jwtService.generateToken(c.getUname());
				log.info("Exiting loginCheck() method : ");
				return new ResponseEntity<>(jwtToken, HttpStatus.OK);
			}

		} catch (Exception e) {
			// logger
			log.info("Exception occured loginCheck() method : ", e.getMessage());
		}

		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}
	*/
	
	@PostMapping("/login")
	public ResponseEntity<?> loginCheck(@RequestBody AuthRequest request) {
	    log.info("Entering into loginCheck() method : ");

	    UsernamePasswordAuthenticationToken token =
	            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

	    try {
	        Authentication authenticate = authenticationManager.authenticate(token);

	        if (authenticate.isAuthenticated()) {
	            // generate tokens
	            String accessToken = jwtService.generateToken(request.getUsername());
	            String refreshToken = jwtService.generateRefreshToken(request.getUsername());

	            log.info("Exiting loginCheck() method : ");
	            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
	        }

	    } catch (Exception e) {
	        log.error("Exception occurred in loginCheck() method : {}", e.getMessage());
	    }

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
	}

	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
		
		String refreshToken = request.get("refreshToken");

		try {
			String username = jwtService.extractUsername(refreshToken);
			
			if (jwtService.validateToken(refreshToken, customerService.loadUserByUsername(username))) {
				
				String newAccessToken = jwtService.generateToken(username);
				return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
			
			} else {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired, login again.");
			}
			
		} catch (Exception e) {
			
			log.error("Invalid refresh token: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
		}

	}


}
