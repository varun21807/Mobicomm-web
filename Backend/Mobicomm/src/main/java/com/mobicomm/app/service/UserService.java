//package com.mobicomm.app.service;
//
//
//import com.mobicomm.app.model.User;
//import com.mobicomm.app.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Fetch all users
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // Get user by ID
//    public Optional<User> getUserById(String userId) {
//        return userRepository.findById(userId);
//    }
//
//    // Add a new user
//    public User addUser(User user) {
//        return userRepository.save(user);
//    }
//
//    // Update an existing user
//    public User updateUser(String userId, User updatedUser) {
//        return userRepository.findById(userId)
//            .map(existingUser -> {
//                existingUser.setUserName(updatedUser.getUserName());
//                existingUser.setUserEmail(updatedUser.getUserEmail());
//                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
//                existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
//                existingUser.setAccountStatus(updatedUser.getAccountStatus());
//                existingUser.setPlanExpiryDate(updatedUser.getPlanExpiryDate());
//                existingUser.setPlanStatus(updatedUser.getPlanStatus());
//                return userRepository.save(existingUser);
//            })
//            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//    }
//
//    // Delete a user
//    public void deleteUser(String userId) {
//        userRepository.deleteById(userId);
//    }
//}
