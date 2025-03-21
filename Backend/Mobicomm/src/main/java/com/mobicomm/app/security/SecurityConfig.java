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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use JWT, no session
            .authorizeHttpRequests(auth -> auth

                // ✅ Allow public access to recharge APIs (without authentication)
                .requestMatchers(HttpMethod.POST, "/api/purchase/new").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/phone/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/payment/create-order").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/payment/verify-payment").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/payment/recharge-history/**", "/api/payment/active-plans/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/recharge-history/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/recharge-history/expiring-soon").permitAll()
                .requestMatchers(HttpMethod.POST, "/email/send").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/send-notification").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categories", "/api/subcategories", "/api/plans", "/api/ott").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                // ✅ Allow public admin creation
                .requestMatchers(HttpMethod.POST, "/api/admin/add").permitAll() 

                // ❌ Restrict other admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ✅ Restrict category and plan management to admin
                .requestMatchers("/api/categories/**", "/api/subcategories/**", "/api/plans/**", "/api/ott/**").hasRole("ADMIN")

                // ❌ Require authentication for all other APIs
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply CORS to all endpoints
                        .allowedOrigins("http://127.0.0.1:5503", "http://localhost:5503", "http://localhost:3000") // Your frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // Expose JWT token
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
}
