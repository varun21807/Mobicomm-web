package com.mobicomm.app.controller;

import com.mobicomm.app.model.Address;
import com.mobicomm.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Get all addresses (only accessible by admins)
    @GetMapping
    public ResponseEntity<?> getAllAddresses(@RequestHeader("Role") String requesterRole) {
        if (!"ROLE_ADMIN".equals(requesterRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access all addresses.");
        }
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    // Create a new address (for user or admin)
    @PostMapping
    public ResponseEntity<?> saveAddress(@RequestBody Address address) {
        try {
            Address savedAddress = addressService.saveAddress(address);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update an address (Admin can update any, Users can update only their own)
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable String addressId, @RequestBody Address updatedAddress) {
        try {
            Address updated = addressService.updateAddress(addressId, updatedAddress);
            return ResponseEntity.ok(updated);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
