package com.example.signin_project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // The password encoder is still needed by your controller, so we keep it.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // This is the debugging configuration
        http
            // Disable CSRF protection
            .csrf(csrf -> csrf.disable())
            
            // This is the key change:
            .authorizeHttpRequests(auth -> auth
                // Allow ANY request to ANY URL without authentication.
                .anyRequest().permitAll()
            );

        return http.build();
    }
}