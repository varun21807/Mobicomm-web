package com.mobicomm.app.controller;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")
@RestController
@RequestMapping("/api/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping
    public List<Plan> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/{id}")
    public Optional<Plan> getPlanById(@PathVariable String id) {
        return planService.getPlanById(id);
    }

    // ✅ Only Admins can modify plans
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Plan addPlan(@RequestBody Plan plan) {
        return planService.addPlan(plan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Plan updatePlan(@PathVariable String id, @RequestBody Plan updatedPlan) {
        return planService.updatePlan(id, updatedPlan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deactivate")
    public void deactivatePlan(@PathVariable String id) {
        planService.deactivatePlan(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public void activatePlan(@PathVariable String id) {
        planService.activatePlan(id);
    }
}
