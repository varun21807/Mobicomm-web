package com.mobicomm.app.repository;

import com.mobicomm.app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
	
	  List<Payment> findByPurchase_PurchaseId(String purchaseId);
}
