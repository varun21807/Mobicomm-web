package com.mobicomm.app.repository;

import com.mobicomm.app.model.RechargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RechargeHistoryRepository extends JpaRepository<RechargeHistory, String> {
   

	List<RechargeHistory> findByPhoneNumber(String phoneNumber);
}
