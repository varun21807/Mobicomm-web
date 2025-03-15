package com.mobicomm.app.service;

import com.mobicomm.app.model.Admin;
import com.mobicomm.app.model.Address;
import com.mobicomm.app.repository.AdminRepository;
import com.mobicomm.app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private AddressService addressService;
    @Autowired
    public AdminService(AdminRepository adminRepository, AddressRepository addressRepository,
                        BCryptPasswordEncoder passwordEncoder, AddressService addressService) {
        this.adminRepository = adminRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;  // ✅ Now Spring will inject it
    }
    // Method to generate Admin ID in the format MCA-0001
    public String generateAdminId() {
        Admin latestAdmin = adminRepository.findTopByOrderByAdminIdDesc();
        int nextId = 1; // Default to 1 if no Admin exists

        if (latestAdmin != null && latestAdmin.getAdminId() != null) {
            try {
                String numberPart = latestAdmin.getAdminId().substring(4); // Extract number part
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                log.error("Error parsing admin ID: {}", e.getMessage());
            }
        }

        return String.format("MCA-%04d", nextId);
    }

    // Save Admin with associated Addresses
    @Transactional
    public Admin saveAdmin(Admin admin) {
        admin.setAdminId(generateAdminId()); 
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); 

        // Assign an ID to each Address before saving
        for (Address address : admin.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) { 
                address.setAddressId(addressService.generateAddressId()); // ✅ Now this will work
            }
            address.setAdmin(admin);
        }

        // Save the Admin first
        Admin savedAdmin = adminRepository.save(admin);

        // Save the Addresses
        addressRepository.saveAll(admin.getAddresses());

        return savedAdmin;
    }

    // Load user by username for authentication
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        return User.withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }

    // Get Admin details by email
    public Admin getAdminDetails(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }
}
