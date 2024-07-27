package com.naveen.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubController {

	@GetMapping("/")
	public String welcome() {
		return "Welcome to SpringBoot Security by accessing thorugh gitHub";
	}
	
}
