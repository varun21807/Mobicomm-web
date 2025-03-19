package com.mobicomm.app.controller;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.model.User;
import com.mobicomm.app.security.JwtUtil;
import com.mobicomm.app.service.AdminService;
import com.mobicomm.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5503", "http://localhost:5503"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
  @PostMapping("/login")
      public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
       String identifier = credentials.get("identifier");
       String password = credentials.get("password");

       System.out.println("Attempting login for: " + identifier);

       if (identifier == null || identifier.isEmpty()) {
           return ResponseEntity.badRequest().body("Identifier cannot be null or empty.");
       }

       try {
    	   UserDetails userDetails;
    	   Map<String, Object> claims = new HashMap<>();

          if (identifier.contains("@")) { // Admin login
                System.out.println("Logging in as Admin...");
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
                userDetails = adminService.loadUserByUsername(identifier);
                claims.put("role", "ADMIN");
           } else { // User login
              System.out.println("Logging in as User...");
              authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identifier, password));
              userDetails = userService.loadUserByPhoneNumber(identifier);
               claims.put("role", "USER");
               }

            System.out.println("Login successful for: " + identifier);

           String accessToken = jwtUtil.generateAccessToken(identifier, claims);
            String refreshToken = jwtUtil.generateRefreshToken(identifier);

           Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
           tokens.put("refreshToken", refreshToken);

           return ResponseEntity.ok(tokens);
        } catch (Exception e) {
           System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email/phone or password");
       }
    }

    // ✅ Refresh Token Endpoint
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String identifier = jwtUtil.extractIdentifier(refreshToken);

        // ✅ Ensure user still exists
        UserDetails userDetails;
        if (identifier.contains("@")) {
            userDetails = adminService.loadUserByUsername(identifier);
        } else {
            userDetails = userService.loadUserByPhoneNumber(identifier);
        }
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // ✅ Generate new access token
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

        String newAccessToken = jwtUtil.generateAccessToken(identifier, claims);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", refreshToken); // Reuse the same refresh token

        return ResponseEntity.ok(tokens);
    }
}
