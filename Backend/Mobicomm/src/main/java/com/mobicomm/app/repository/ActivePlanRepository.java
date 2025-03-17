package com.mobicomm.app.repository;

import com.mobicomm.app.model.ActivePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivePlanRepository extends JpaRepository<ActivePlan, String> {
    

	List<ActivePlan> findByPhoneNumber(String phoneNumber);
}
