package com.mobicomm.app.service;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // ✅ Add Admin
    public Admin addAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Admin with this email already exists.");
        }
        return adminRepository.save(admin);
    }

    // ✅ Get Admin by ID
    public Optional<Admin> getAdminById(String adminId) {
        return adminRepository.findById(adminId);
    }

    // ✅ Get Admin by Email
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    // ✅ Update Admin
    public Admin updateAdmin(String adminId, Admin updatedAdmin) {
        return adminRepository.findById(adminId)
            .map(existingAdmin -> {
                existingAdmin.setEmail(updatedAdmin.getEmail());
                existingAdmin.setPassword(updatedAdmin.getPassword());
                return adminRepository.save(existingAdmin);
            })
            .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));
    }

    // ✅ Delete Admin
    public void deleteAdmin(String adminId) {
        adminRepository.deleteById(adminId);
    }
}
