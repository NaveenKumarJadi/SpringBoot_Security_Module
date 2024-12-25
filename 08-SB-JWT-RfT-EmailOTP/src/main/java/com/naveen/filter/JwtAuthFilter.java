package com.naveen.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.naveen.config.UserInfoUserDetailsService;
import com.naveen.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
The JwtAuthFilter class is a Spring Security filter designed to process every incoming HTTP request, validate the JWT token, 
and set the authentication context for the request. 
It extends OncePerRequestFilter, ensuring that the filter is executed only once per request. Here's a detailed explanation:
*/

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

//	public JwtAuthFilter(HandlerExceptionResolver exceptionResolver) {
//		this.exceptionResolver = exceptionResolver;
//	}

//	@Autowired
//	private HandlerExceptionResolver exceptionResolver;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserInfoUserDetailsService userDetailsService;

	// The core logic of the filter is implemented here. It is invoked for every HTTP request.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Retrieve the Authorization header
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		try {
			// Check if the header starts with "Bearer "
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7); // Extract the token by removing "Bearer "
				username = jwtService.extractUsername(token); // Extract username from the token
			}

			// If the token is valid and no authentication is set in the context
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// Validate token and set authentication
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			// Continue the filter chain
			filterChain.doFilter(request, response);

		} catch (Exception ex) {
//			exceptionResolver.resolveException(request, response, null, ex);
			ex.getMessage();
		}
	}
}
