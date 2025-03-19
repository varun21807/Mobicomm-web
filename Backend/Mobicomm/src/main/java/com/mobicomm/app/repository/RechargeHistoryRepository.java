package com.mobicomm.app.repository;

import com.mobicomm.app.model.RechargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface RechargeHistoryRepository extends JpaRepository<RechargeHistory, String> {
	List<RechargeHistory> findByExpiryDateBefore(LocalDateTime dateTime);

	List<RechargeHistory> findByPhoneNumber(String phoneNumber);
	 List<RechargeHistory> findByPhoneNumberOrderByExpiryDateDesc(String phoneNumber);
}
