package com.mobicomm.app.controller;

import com.mobicomm.app.model.Plan;
import com.mobicomm.app.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
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

    @PostMapping
    public Plan addPlan(@RequestBody Plan plan) {
        return planService.addPlan(plan);
    }

    @PutMapping("/{id}")
    public Plan updatePlan(@PathVariable String id, @RequestBody Plan updatedPlan) {
        return planService.updatePlan(id, updatedPlan);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
    }
    @PutMapping("/{id}/deactivate")
    public void deactivatePlan(@PathVariable String id) {  // ✅ Use `id`
        planService.deactivatePlan(id);
    }

    @PutMapping("/{id}/activate")
    public void activatePlan(@PathVariable String id) {  // ✅ Use `id`
        planService.activatePlan(id);
    }

}
