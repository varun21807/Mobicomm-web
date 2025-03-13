package com.mobicomm.app.repository;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Subcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {

    // ✅ Get the last inserted plan by ID in descending order
    Optional<Plan> findTopByOrderByPlanIdDesc();

    List<Plan> findByPlanNameContainingIgnoreCase(String planName);

    List<Plan> findByPriceBetween(Long minPrice, Long maxPrice);
    List<Plan> findBySubcategory(Subcategory subcategory);
}
