package com.mobicomm.app.controller;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.security.JwtUtil;
import com.mobicomm.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService; // ✅ Injected AdminService

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        try {
            // ✅ Authenticate using Spring Security
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword())
            );

            // ✅ Generate JWT Token
            String token = jwtUtil.generateToken(admin.getEmail());

            // ✅ Return JWT Token in JSON format
            return ResponseEntity.ok("{ \"token\": \"" + token + "\" }");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
    }
}
