package com.mobicomm.app.repository;

import com.mobicomm.app.model.NewUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NewUserRepository extends JpaRepository<NewUser, String> {
    Optional<NewUser> findByNewUserId(String newUserId);
    NewUser findTopByOrderByNewUserIdDesc();
}
