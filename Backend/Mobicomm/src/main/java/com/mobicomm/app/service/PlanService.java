package com.mobicomm.app.service;

import com.mobicomm.app.model.Ott;
import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Status;
import com.mobicomm.app.model.Subcategory;
import com.mobicomm.app.repository.OttRepository;
import com.mobicomm.app.repository.PlanRepository;
import com.mobicomm.app.repository.SubcategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private OttRepository ottRepository;
    public String generatePlanId() {
        // Fetch the latest plan entry based on descending order of ID
        Optional<Plan> latestPlanOpt = planRepository.findTopByOrderByPlanIdDesc();

        // Default to 1 if no plans exist
        int nextId = 1;  
        if (latestPlanOpt.isPresent()) {
            String latestPlanId = latestPlanOpt.get().getPlanId(); // Expected format: MCP-XXXX
            String numberPart = latestPlanId.substring(4); // Extract numeric part

            try {
                nextId = Integer.parseInt(numberPart) + 1; // Increment number part
            } catch (NumberFormatException e) {
                nextId = 1; // Fallback if parsing fails
            }
        }

        // Generate the new Plan ID
        return String.format("MCP-%04d", nextId);
    }

    
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Optional<Plan> getPlanById(String planId) {
        return planRepository.findById(planId);
    }

    public List<Plan> getActivePlans() {
        return planRepository.findByPlanStatus(Status.ACTIVE);
    }

    public List<Plan> getInactivePlans() {
        return planRepository.findByPlanStatus(Status.INACTIVE);
    }
    public Plan addPlan(Plan plan) {
        plan.setPlanId(generatePlanId()); // ✅ Ensure unique ID}

        return planRepository.save(plan);
    }



    public Plan updatePlan(String planId, Plan updatedPlan) {
        Plan existingPlan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        existingPlan.setPlanName(updatedPlan.getPlanName());
        existingPlan.setPrice(updatedPlan.getPrice());
        existingPlan.setValidity(updatedPlan.getValidity());
        existingPlan.setData(updatedPlan.getData());
        existingPlan.setCalls(updatedPlan.getCalls());
        existingPlan.setSms(updatedPlan.getSms());
        existingPlan.setBenefits(updatedPlan.getBenefits());
        existingPlan.setOffer(updatedPlan.getOffer());
        existingPlan.setBadgeColor(updatedPlan.getBadgeColor());
        existingPlan.setSubcategory(updatedPlan.getSubcategory());

        // ✅ Ensure OTTs are correctly updated
        existingPlan.getOttPlatforms().clear();
        existingPlan.getOttPlatforms().addAll(updatedPlan.getOttPlatforms());

        return planRepository.save(existingPlan);
    }

    public void deletePlan(String planId) {
        planRepository.deleteById(planId);
    }
    public void deactivatePlan(String planId) {
        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            Plan plan = optionalPlan.get();
            plan.setPlanStatus(Status.INACTIVE);
            planRepository.save(plan);
        } else {
            throw new RuntimeException("Plan not found with ID: " + planId);
        }
    }

    // ✅ Activate a Specific Plan
    public void activatePlan(String planId) {
        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            Plan plan = optionalPlan.get();
            plan.setPlanStatus(Status.ACTIVE);
            planRepository.save(plan);
        } else {
            throw new RuntimeException("Plan not found with ID: " + planId);
        }
    }
    public void removeOttFromPlan(String planId, String ottId) {
        Plan plan = planRepository.findById(planId)
                      .orElseThrow(() -> new RuntimeException("Plan not found"));

        Ott ott = ottRepository.findById(ottId)
                    .orElseThrow(() -> new RuntimeException("OTT Platform not found"));

        plan.getOttPlatforms().remove(ott); // ✅ Remove OTT
        planRepository.save(plan); // ✅ Save updated plan
    }

}
