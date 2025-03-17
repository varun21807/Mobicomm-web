package com.mobicomm.app.controller;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ Add a new admin with addresses (Restricted to ADMIN)
    @PostMapping("/add")
  
    public ResponseEntity<String> addAdmin(@RequestBody Admin admin) {
        adminService.saveAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin and addresses added successfully");
    }

    // ✅ Update an existing admin and addresses
    @PutMapping("/update/{adminId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateAdmin(@PathVariable String adminId, @RequestBody Admin updatedAdmin) {
        Optional<Admin> existingAdmin = adminService.getAdminById(adminId);
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
        
        Admin updated = adminService.updateAdmin(adminId, updatedAdmin);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete an admin and associated addresses
    @DeleteMapping("/delete/{adminId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable String adminId) {
        Optional<Admin> existingAdmin = adminService.getAdminById(adminId);
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Secure admin dashboard endpoint
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }
}
