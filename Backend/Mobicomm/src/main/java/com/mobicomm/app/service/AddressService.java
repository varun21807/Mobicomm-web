//package com.mobicomm.app.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mobicomm.app.model.Address;
//import com.mobicomm.app.repository.AddressRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//class AddressService {
//	@Autowired
//    private final AddressRepository addressRepository;
//    
//    public List<Address> getAllAddresses() {
//        return addressRepository.findAll();
//    }
//    
//    public Address saveAddress(Address address) {
//        if (address.getUser() != null) {
//            address.setNewUser(null);
//        } else if (address.getNewUser() != null) {
//            address.setUser(null);
//        }
//        return addressRepository.save(address);
//    }
//}