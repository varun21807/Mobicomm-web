package com.mobicomm.app.controller;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ✅ Add Admin
    @PostMapping("/add")
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    // ✅ Get Admin by ID
    @GetMapping("/{adminId}")
    public Optional<Admin> getAdminById(@PathVariable String adminId) {
        return adminService.getAdminById(adminId);
    }

    // ✅ Get Admin by Email
    @GetMapping("/email/{email}")
    public Optional<Admin> getAdminByEmail(@PathVariable String email) {
        return adminService.getAdminByEmail(email);
    }

    // ✅ Update Admin
    @PutMapping("/update/{adminId}")
    public Admin updateAdmin(@PathVariable String adminId, @RequestBody Admin updatedAdmin) {
        return adminService.updateAdmin(adminId, updatedAdmin);
    }

    // ✅ Delete Admin
    @DeleteMapping("/delete/{adminId}")
    public String deleteAdmin(@PathVariable String adminId) {
        adminService.deleteAdmin(adminId);
        return "Admin deleted successfully!";
    }
}
