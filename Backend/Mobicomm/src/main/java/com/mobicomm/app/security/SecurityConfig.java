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
                // ✅ Public Endpoints - No authentication required
                .requestMatchers(HttpMethod.GET, "/api/categories", "/api/subcategories", "/api/plans", "/api/ott").permitAll()
                .requestMatchers("/auth/**").permitAll() // Login
                .requestMatchers(HttpMethod.POST, "/api/purchase/new").permitAll() // Recharge without login
                
                // ✅ Allow user-related requests
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()

                // ✅ Purchase Controller - Secure user purchase history & active plans
                .requestMatchers(HttpMethod.GET, "/api/purchase/history", "/api/purchase/active-plans").authenticated()

                // ✅ Allow Admin Registration (Fix for 403 error)
                .requestMatchers(HttpMethod.POST, "/api/admin/add").permitAll()

                // ✅ Secure Admin Endpoints - Require ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  
                .requestMatchers("/api/categories/**", "/api/subcategories/**", "/api/plans/**", "/api/ott/**").hasRole("ADMIN")

                // ✅ Allow OPTIONS requests (Fix CORS issues)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Allow Payment API (CashfreeController)
                .requestMatchers(HttpMethod.POST, "/api/payment/create-order").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/payment/status/**").permitAll()

                // ✅ 🔥 Fix 403 Forbidden: Allow Webhook Endpoint
                .requestMatchers(HttpMethod.POST, "/api/payment/webhook").permitAll()

                // ✅ Secure all other endpoints - Require authentication
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
                        .allowedOrigins("http://127.0.0.1:5503") // Your frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization") // Expose JWT token
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
}
