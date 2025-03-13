package com.mobicomm.app.repository;

import com.mobicomm.app.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByEmail(String email); // ✅ Find admin by email for login
}
