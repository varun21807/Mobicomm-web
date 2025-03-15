package com.mobicomm.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mobicomm.app.model.Admin;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}
