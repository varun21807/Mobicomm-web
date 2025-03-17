package com.mobicomm.app.service;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.model.Address;
import com.mobicomm.app.repository.AdminRepository;
import com.mobicomm.app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AddressService addressService;


    public String generateAdminId() {
        Admin latestAdmin = adminRepository.findTopByOrderByAdminIdDesc();
        int nextId = 1;
        if (latestAdmin != null && latestAdmin.getAdminId() != null) {
            try {
                String numberPart = latestAdmin.getAdminId().substring(4);
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                log.error("Error parsing admin ID: {}", e.getMessage());
            }
        }
        return String.format("MCA-%04d", nextId);
    }
    public Optional<Admin> getAdminById(String adminId) {
        return adminRepository.findById(adminId);
    }
    @Transactional
    public Admin saveAdmin(Admin admin) {
        admin.setAdminId(generateAdminId());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        for (Address address : admin.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setAdmin(admin);
        }
        Admin savedAdmin = adminRepository.save(admin);
        addressRepository.saveAll(admin.getAddresses());
        return savedAdmin;
    }

    @Transactional
    public Admin updateAdmin(String adminId, Admin updatedAdmin) {
        Admin existingAdmin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with ID: " + adminId));
        existingAdmin.setEmail(updatedAdmin.getEmail());
        existingAdmin.setName(updatedAdmin.getName());
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }
        for (Address address : updatedAdmin.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setAdmin(existingAdmin);
        }
        addressRepository.saveAll(updatedAdmin.getAddresses());
        return adminRepository.save(existingAdmin);
    }

    @Transactional
    public void deleteAdmin(String adminId) {
        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with ID: " + adminId));
        addressRepository.deleteAll(admin.getAddresses());
        adminRepository.delete(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
        return User.withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }

    public Admin getAdminDetails(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }
}
