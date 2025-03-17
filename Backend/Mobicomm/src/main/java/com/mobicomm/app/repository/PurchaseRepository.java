package com.mobicomm.app.repository;

import com.mobicomm.app.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {
	List<Purchase> findByPhoneNumber(String phoneNumber);

    
}
