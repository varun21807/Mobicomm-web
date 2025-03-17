package com.mobicomm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobicomm.app.model.Admin;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
	 Admin findTopByOrderByAdminIdDesc();
	 Optional<Admin> findByAdminId(String adminId); // ✅ Add this method
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByName(String name);
}

