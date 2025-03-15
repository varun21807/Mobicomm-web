package com.mobicomm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mobicomm.app.model.Admin;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
	 Admin findTopByOrderByAdminIdDesc();
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByName(String name);
}

