package com.mobicomm.app.service;

import com.mobicomm.app.model.Category;
import com.mobicomm.app.model.Plan;
import com.mobicomm.app.model.Status;
import com.mobicomm.app.model.Subcategory;
import com.mobicomm.app.repository.CategoryRepository;
import com.mobicomm.app.repository.PlanRepository;
import com.mobicomm.app.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PlanRepository planRepository;


 // Method to generate Subcategory ID in the format MCS-0001
    public String generateSubcategoryId() {
        // Fetch the latest subcategory entry in descending order
        Optional<Subcategory> latestSubcategoryOpt = subcategoryRepository.findTopByOrderBySubcategoryIdDesc();

        // Default ID if no subcategories exist
        int nextId = 1;

        if (latestSubcategoryOpt.isPresent()) {
            Subcategory latestSubcategory = latestSubcategoryOpt.get();
            String latestSubcategoryId = latestSubcategory.getSubcategoryId(); // Expected format: MCS-XXXX

            if (latestSubcategoryId != null && latestSubcategoryId.startsWith("MCS-")) {
                String numberPart = latestSubcategoryId.substring(4); // Extract numeric part
                try {
                    nextId = Integer.parseInt(numberPart) + 1; // Increment number part
                } catch (NumberFormatException e) {
                    nextId = 1; // Reset if parsing fails
                }
            }
        }

        // Generate the new Subcategory ID
        return String.format("MCS-%04d", nextId);
    }


    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    public Optional<Subcategory> getSubcategoryById(String subcategoryId) {
        return subcategoryRepository.findById(subcategoryId);
    }

    public Subcategory addSubcategory(Subcategory subcategory) {
        // ✅ Check if the category exists before adding subcategory
    	subcategory.setSubcategoryId(generateSubcategoryId());
        String categoryId = subcategory.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category with ID " + categoryId + " does not exist!"));

        subcategory.setCategory(category); // Ensure valid category reference
        if (subcategory.getStatus() == null) {
            subcategory.setStatus(Status.ACTIVE);
        }
        return subcategoryRepository.save(subcategory);
    }
    public Subcategory updateSubcategory(String subcategoryId, Subcategory updatedSubcategory) {
        return subcategoryRepository.findById(subcategoryId)
            .map(existingSubcategory -> {
                existingSubcategory.setName(updatedSubcategory.getName());
                existingSubcategory.setStatus(updatedSubcategory.getStatus());
                existingSubcategory.setCategory(updatedSubcategory.getCategory());
                return subcategoryRepository.save(existingSubcategory);
            })
            .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + subcategoryId));
    }
   

    public List<Subcategory> getSubcategoriesByStatus(Status status) {
        return subcategoryRepository.findByStatus(status);
    }

    public List<Subcategory> getSubcategoriesByCategory(String categoryId) {
        return subcategoryRepository.findByCategory_CategoryId(categoryId);
    }

    public List<Subcategory> getActiveSubcategoriesByCategory(String categoryId) {
        return subcategoryRepository.findActiveSubcategoriesByCategory(categoryId);
    }
    public void deleteSubcategory(String subcategoryId) {
        subcategoryRepository.deleteById(subcategoryId);
    }
    public void deactivateSubcategory(String subcategoryId) {
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(subcategoryId);
        if (optionalSubcategory.isPresent()) {
            Subcategory subcategory = optionalSubcategory.get();
            subcategory.setStatus(Status.INACTIVE);
            subcategoryRepository.save(subcategory);

            // Find all plans linked to this subcategory and deactivate them
            List<Plan> plans = planRepository.findBySubcategory(subcategory);
            for (Plan plan : plans) {
                plan.setPlanStatus(Status.INACTIVE);
                planRepository.save(plan);
            }
        } else {
            throw new RuntimeException("Subcategory not found with ID: " + subcategoryId);
        }
    }

    // ✅ Activate Subcategory and its associated Plans
    public void activateSubcategory(String subcategoryId) {
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(subcategoryId);
        if (optionalSubcategory.isPresent()) {
            Subcategory subcategory = optionalSubcategory.get();
            subcategory.setStatus(Status.ACTIVE);
            subcategoryRepository.save(subcategory);

            // Find all plans linked to this subcategory and activate them
            List<Plan> plans = planRepository.findBySubcategory(subcategory);
            for (Plan plan : plans) {
                plan.setPlanStatus(Status.ACTIVE);
                planRepository.save(plan);
            }
        } else {
            throw new RuntimeException("Subcategory not found with ID: " + subcategoryId);
        }
    }
}
