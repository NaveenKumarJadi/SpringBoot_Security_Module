package com.naveen.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageRestController {
	
	@GetMapping("/welcome")
	public String welcomeMsg() {
		return "Welcome to Spring Security";
	}
	
	@GetMapping("/admin")
	public String adminMethod() {
		return "Only Admin can have the access";
	}
	
	@GetMapping("/user")
	public String userMethod() {
		return "only User can have the access";
	}
	
	@GetMapping("/about")
	public String about() {
		return "Thanks for visiting our Bank.!";
	}

}