package com.mobicomm.app.service;

import com.mobicomm.app.model.NewUser;
import com.mobicomm.app.model.Address;
import com.mobicomm.app.repository.NewUserRepository;
import com.mobicomm.app.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewUserService {

    private final NewUserRepository newUserRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;

    public String generateNewUserId() {
        NewUser latestUser = newUserRepository.findTopByOrderByNewUserIdDesc();
        int nextId = 1;
        if (latestUser != null && latestUser.getNewUserId() != null) {
            try {
                String numberPart = latestUser.getNewUserId().substring(5);
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                log.error("Error parsing new user ID: {}", e.getMessage());
            }
        }
        return String.format("MCNU-%04d", nextId);
    }

    public Optional<NewUser> getNewUserById(String newUserId) {
        return newUserRepository.findById(newUserId);
    }

    @Transactional
    public NewUser saveNewUser(NewUser newUser) {
        newUser.setNewUserId(generateNewUserId());
        for (Address address : newUser.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setNewUser(newUser);
        }
        NewUser savedUser = newUserRepository.save(newUser);
        addressRepository.saveAll(newUser.getAddresses());
        return savedUser;
    }

    @Transactional
    public NewUser updateNewUser(String newUserId, NewUser updatedNewUser) {
        NewUser existingNewUser = newUserRepository.findByNewUserId(newUserId)
                .orElseThrow(() -> new RuntimeException("New User not found with ID: " + newUserId));
        existingNewUser.setEmail(updatedNewUser.getEmail());
        existingNewUser.setNewUserName(updatedNewUser.getNewUserName());
        existingNewUser.setPhoneNumber(updatedNewUser.getPhoneNumber());
        for (Address address : updatedNewUser.getAddresses()) {
            if (address.getAddressId() == null || address.getAddressId().isEmpty()) {
                address.setAddressId(addressService.generateAddressId());
            }
            address.setNewUser(existingNewUser);
        }
        addressRepository.saveAll(updatedNewUser.getAddresses());
        return newUserRepository.save(existingNewUser);
    }

    @Transactional
    public void deleteNewUser(String newUserId) {
        NewUser newUser = newUserRepository.findByNewUserId(newUserId)
                .orElseThrow(() -> new RuntimeException("New User not found with ID: " + newUserId));
        addressRepository.deleteAll(newUser.getAddresses());
        newUserRepository.delete(newUser);
    }
}