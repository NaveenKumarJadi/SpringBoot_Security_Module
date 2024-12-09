package com.naveen.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // User Creation
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        
    	// InMemoryUserDetailsManager setup with three users
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin@123"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withUsername("user")
                .password(encoder.encode("user@123"))
                .roles("USER")
                .build();
        
//        UserDetails commander = User.builder()
//        		.username("bujji")
//        		.password(encoder.encode("bujji@123"))
//        		.roles("BUJJI")
//        		.build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/welcome").permitAll() // Permit all access to /auth/welcome
                .requestMatchers("/auth/user/**").authenticated() // Require authentication for /auth/user/**
                .requestMatchers("/auth/admin/**").authenticated() // Require authentication for /auth/admin/**
//                .requestMatchers("/auth/come/**").authenticated()
            )
            .formLogin(Customizer.withDefaults()); // Enable form-based login
        
        return http.build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}