package com.mobicomm.app.repository;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Status;
import com.mobicomm.app.model.Subcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {

	@Query("SELECT p FROM Plan p ORDER BY CAST(SUBSTRING(p.planId, 5) AS int) DESC LIMIT 1")
	Optional<Plan> findTopByOrderByPlanIdDesc();

	
	    List<Plan> findByPlanStatus(Status planStatus);

	   
	    @Query("SELECT p FROM Plan p WHERE p.planStatus = :status")
	    List<Plan> findPlansByStatus(@Param("status") Status status);

	 
	    @Query(value = "SELECT * FROM plan WHERE plan_status = :status", nativeQuery = true)
	    List<Plan> findPlansByStatusNative(@Param("status") String status);
    List<Plan> findByPlanNameContainingIgnoreCase(String planName);

    List<Plan> findByPriceBetween(Long minPrice, Long maxPrice);
    List<Plan> findBySubcategory(Subcategory subcategory);
}
