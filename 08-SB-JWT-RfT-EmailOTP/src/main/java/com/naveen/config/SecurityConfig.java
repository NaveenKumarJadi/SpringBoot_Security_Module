package com.naveen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.naveen.filter.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
//	  Because of Exception we are doing changes
    @Autowired
    private JwtAuthFilter authFilter;

//	@Autowired
//	@Qualifier("HandlerExceptionResolver")
//	public HandlerExceptionResolver exceptionResolver;
	
	//Authentication
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService(); // Ensure UserInfoService implements UserDetailsService
    }

//    @Bean
//    public JwtAuthFilter authFilter() {
//    	return new JwtAuthFilter(exceptionResolver);
//    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
    	/*
    	// Depricated
    	return http.csrf().disable() // CSRF is disabled for stateless APIs since there is no session.
                .authorizeHttpRequests()
                .requestMatchers("/products/signUp","/products/login","/products/refreshToken").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/products/**")
                .authenticated().and()// Protect all other endpoints
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures stateless session management using SessionCreationPolicy.STATELESS.
                .and()
                .authenticationProvider(authenticationProvider()) // Custom authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .build();
        */
        
    	/*
    	 http
            .csrf(csrf -> csrf.disable()) // CSRF is disabled for stateless APIs since there is no session.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/products/signUp","/products/login","/products/refreshToken").permitAll()
                .requestMatchers("/products/**").hasAuthority("ROLE_USER")
                .requestMatchers("/products/all).hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated() // Protect all other endpoints
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures stateless session management using SessionCreationPolicy.STATELESS.
            )
            .authenticationProvider(authenticationProvider()) // Custom authentication provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    	 */
    	
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{ auth
                	.requestMatchers("/products/signUp","/products/login","/products/refreshToken", "/products/verify-email", "/products/resend-otp").permitAll()
                    .requestMatchers("/products/**").authenticated();
                })
                .sessionManagement(session->session
                		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder for hashing passwords securely.
    }

    // Delegates user data loading to UserDetailsService. Uses BCryptPasswordEncoder to validate passwords.
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    // Retrieves the AuthenticationManager from AuthenticationConfiguration, which handles the authentication process.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}