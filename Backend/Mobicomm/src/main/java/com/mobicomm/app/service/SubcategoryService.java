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

    // ✅ Generate next subcategory ID in format "sub_1", "sub_2"
    private String generateSubcategoryId() {
        Subcategory lastSubcategory = subcategoryRepository.findTopByOrderBySubcategoryIdDesc();
        
        if (lastSubcategory != null) {
            String lastId = lastSubcategory.getSubcategoryId(); // e.g., "sub_10"
            int lastNumber = Integer.parseInt(lastId.replace("sub_", "")); // Extract numeric part
            return "sub_" + (lastNumber + 1); // Generate new ID
        } else {
            return "sub_1"; // First record
        }
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
