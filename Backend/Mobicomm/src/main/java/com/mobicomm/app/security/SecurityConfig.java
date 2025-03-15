package com.mobicomm.app.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.cors(Customizer.withDefaults()) // Enable CORS
            .csrf().disable() // Disable CSRF (useful for stateless authentication)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
            .authorizeHttpRequests(auth -> auth
                // Public Endpoints - No authentication needed
                .requestMatchers(HttpMethod.GET, "/api/categories", "/api/subcategories", "/api/plans", "/api/ott").permitAll()

                // Admin-related Endpoints - Require ADMIN role
                .requestMatchers("/api/categories/**", "/api/subcategories/**", "/api/plans/**", "/api/ott/**")
                .hasRole("ADMIN") // Ensure roles are prefixed with "ROLE_" in the database

                // Authentication endpoints - Allow anyone to access login/signup
                .requestMatchers("/auth/**").permitAll()

                // Add Admin - Allow everyone to add admins (no authentication required)
                .requestMatchers(HttpMethod.POST, "/api/admin/add").permitAll() 

                // Secure all other endpoints - Require authentication for everything else
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the authentication filter

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("http://127.0.0.1:5503") // Update this with your frontend URL (e.g., http://localhost:3000 or your production frontend URL)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // Expose Authorization header to the frontend
                        .allowCredentials(true); // Allow credentials (cookies, sessions)
            }
        };
    }
}
