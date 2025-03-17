package com.mobicomm.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobicomm.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findTopByOrderByUserIdDesc();
    Optional<User> findByUserEmail(String userEmail);
   
    Optional<User> findByUserName(String userName);
    Optional<User> findByPhoneNumber(String phoneNumber);


}

