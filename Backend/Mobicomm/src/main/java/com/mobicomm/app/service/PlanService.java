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


    // ✅ Generate next plan ID in format "pl_1", "pl_2", "pl_3"
    private String generatePlanId() {
        Optional<Plan> lastPlan = planRepository.findTopByOrderByPlanIdDesc();
        
        if (lastPlan.isPresent()) {
            String lastId = lastPlan.get().getPlanId(); // e.g., "pl_10"
            int lastNumber = Integer.parseInt(lastId.replace("pl_", "")); // Extract numeric part
            return "pl_" + (lastNumber + 1); // Generate new ID
        } else {
            return "pl_1"; // First record
        }
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Optional<Plan> getPlanById(String planId) {
        return planRepository.findById(planId);
    }

    
    public Plan addPlan(Plan plan) {
        plan.setPlanId(generatePlanId()); // ✅ Ensure unique ID}

        return planRepository.save(plan);
    }



    public Plan updatePlan(String planId, Plan updatedPlan) {
        return planRepository.findById(planId)
            .map(existingPlan -> {
                existingPlan.setPlanName(updatedPlan.getPlanName());
                existingPlan.setPrice(updatedPlan.getPrice());
                existingPlan.setDuration(updatedPlan.getDuration());
                existingPlan.setData(updatedPlan.getData());
                existingPlan.setCalls(updatedPlan.getCalls());
                existingPlan.setSms(updatedPlan.getSms());
                existingPlan.setBenefits(updatedPlan.getBenefits());
                existingPlan.setOffer(updatedPlan.getOffer());
                existingPlan.setBadgeColor(updatedPlan.getBadgeColor());
                existingPlan.setPlanStatus(updatedPlan.getPlanStatus());
                return planRepository.save(existingPlan);
            })
            .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + planId));
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
