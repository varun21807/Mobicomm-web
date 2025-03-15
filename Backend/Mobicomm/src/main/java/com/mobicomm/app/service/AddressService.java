package com.mobicomm.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mobicomm.app.model.Address;
import com.mobicomm.app.model.Admin;
import com.mobicomm.app.model.User;
import com.mobicomm.app.repository.AddressRepository;
import com.mobicomm.app.repository.AdminRepository;
import com.mobicomm.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public String generateAddressId() {
        Address latestAddress = addressRepository.findTopByOrderByAddressIdDesc();

        int nextId = 1;
        if (latestAddress != null && latestAddress.getAddressId() != null) {
            String latestAddressId = latestAddress.getAddressId();
            String numberPart = latestAddressId.substring(5);

            try {
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextId = 1;
            }
        }

        return String.format("MCAD-%04d", nextId);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address saveAddress(Address address) {
        if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
            address.setAddressId(generateAddressId());
        }

        validateAddressRelationships(address);
        return addressRepository.save(address);
    }

    public Address updateAddress(String addressId, Address updatedAddress) {
        Address existingAddress = addressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (existingAddress.getAdmin() != null) {
            Optional<Admin> admin = adminRepository.findByName(loggedInUsername);
            if (admin.isEmpty() || !admin.get().equals(existingAddress.getAdmin())) {
                throw new SecurityException("Only the admin can update this address.");
            }
        } else if (existingAddress.getUser() != null) {
            Optional<User> user = userRepository.findByUserName(loggedInUsername);
            if (user.isEmpty() || !user.get().equals(existingAddress.getUser())) {
                throw new SecurityException("Only the user can update this address.");
            }
        }

        existingAddress.setStreet(updatedAddress.getStreet());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPincode(updatedAddress.getPincode());
        existingAddress.setCountry(updatedAddress.getCountry());

        return addressRepository.save(existingAddress);
    }

    private void validateAddressRelationships(Address address) {
        int relationships = 0;

        if (address.getUser() != null) relationships++;
        if (address.getNewUser() != null) relationships++;
        if (address.getAdmin() != null) relationships++;

        if (relationships > 1) {
            throw new IllegalArgumentException("Address cannot have more than one relationship (User, NewUser, or Admin).");
        }

        if (relationships == 0) {
            throw new IllegalArgumentException("Address must be linked to at least one entity (User, NewUser, or Admin).");
        }

        if (address.getUser() != null) {
            address.setNewUser(null);
            address.setAdmin(null);
        } else if (address.getNewUser() != null) {
            address.setUser(null);
            address.setAdmin(null);
        } else if (address.getAdmin() != null) {
            address.setUser(null);
            address.setNewUser(null);
        }
    }
}
