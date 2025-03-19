package com.mobicomm.app.service;

import com.mobicomm.app.model.User;
import com.mobicomm.app.model.Address;
import com.mobicomm.app.repository.UserRepository;
import com.mobicomm.app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder; // ✅ Added password encoder

    public String generateUserId() {
        User latestUser = userRepository.findTopByOrderByUserIdDesc();
        int nextId = 1;
        if (latestUser != null && latestUser.getUserId() != null) {
            try {
                String numberPart = latestUser.getUserId().substring(4);
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                log.error("Error parsing user ID: {}", e.getMessage());
            }
        }
        return String.format("MCU-%04d", nextId);
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {
        user.setUserId(generateUserId());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        for (Address address : user.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setUser(user);
        }

        User savedUser = userRepository.save(user);
        addressRepository.saveAll(user.getAddresses());
        return savedUser;
    }

    @Transactional
    public User updateUser(String userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setUserEmail(updatedUser.getUserEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setStatus(updatedUser.getStatus());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        for (Address address : updatedUser.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setUser(existingUser);
        }

        addressRepository.saveAll(updatedUser.getAddresses());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        addressRepository.deleteAll(user.getAddresses());
        userRepository.delete(user);
    }

    public UserDetails loadUserByPhoneNumber(String phoneNumber) {
        System.out.println("Loading user with phone number: " + phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber)
                      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        System.out.println("User found: " + user.getPhoneNumber());

        return new org.springframework.security.core.userdetails.User(
            user.getPhoneNumber(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }


}
