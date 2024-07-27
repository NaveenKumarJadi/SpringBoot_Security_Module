package com.naveen.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeContorller {

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Spring Security";
	}

	@GetMapping("/wish")
	public String greetings() {
		return "All the Best..!";
	}
}

//	http://localhost:8080/welcome

//	http://localhost:8080/wish