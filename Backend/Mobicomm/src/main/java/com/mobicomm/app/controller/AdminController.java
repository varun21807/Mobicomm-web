package com.mobicomm.app.controller;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.repository.AdminRepository;
import com.mobicomm.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    // ✅ Get all admins (Only for authorized admins)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
 // Only admins can access this
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminRepository.findAll());
    }

    // ✅ Add a new admin (For testing purposes)
    @PostMapping("/add")
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        adminService.saveAdmin(admin.getEmail(), admin.getPassword());
        return ResponseEntity.ok("Admin added successfully");
    }


    // ✅ Secure endpoint - Can only be accessed if JWT token is provided
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
 // Securing with role-based authorization
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }
}
