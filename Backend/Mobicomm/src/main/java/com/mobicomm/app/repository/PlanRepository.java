package com.mobicomm.app.repository;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Subcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {

	 @Query("SELECT p FROM Plan p ORDER BY CAST(SUBSTRING(p.planId, 5) AS integer) DESC")
	    Plan findTopByOrderByPlanIdDesc();

    List<Plan> findByPlanNameContainingIgnoreCase(String planName);

    List<Plan> findByPriceBetween(Long minPrice, Long maxPrice);
    List<Plan> findBySubcategory(Subcategory subcategory);
}
