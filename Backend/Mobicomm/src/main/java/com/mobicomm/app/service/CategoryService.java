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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;
 // Method to generate Category ID in the format MCC-0001
    public String generateCategoryId() {
        // Fetch the latest category entry based on descending order of ID
        Category latestCategory = categoryRepository.findTopByOrderByCategoryIdDesc();

        // Default to 1 if no categories exist
        int nextId = 1;  
        if (latestCategory != null) {
            String latestCategoryId = latestCategory.getCategoryId(); // Expected format: MCC-XXXX
            String numberPart = latestCategoryId.substring(4); // Extract numeric part

            try {
                nextId = Integer.parseInt(numberPart) + 1; // Increment number part
            } catch (NumberFormatException e) {
                nextId = 1;
            }
        }

        // Generate the new Category ID
        return String.format("MCC-%04d", nextId);
    }
    public List<Category> getActiveCategories() {
        return categoryRepository.findByStatus(Status.ACTIVE);
    }

    public List<Category> getInactiveCategories() {
        return categoryRepository.findByStatus(Status.INACTIVE);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category addCategory(Category category) {
        category.setCategoryId(generateCategoryId()); // ✅ Assign generated ID before saving
        if (category.getStatus() == null) {
            category.setStatus(Status.ACTIVE);
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(String categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId)
            .map(existingCategory -> {
                existingCategory.setName(updatedCategory.getName());
                existingCategory.setStatus(updatedCategory.getStatus());
                return categoryRepository.save(existingCategory);
            })
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));
    }

    public void deleteCategory(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }
    public void activateCategory(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            
            // Activate the category
            category.setStatus(Status.ACTIVE);
            
            // Activate all associated subcategories
            for (Subcategory subcategory : category.getSubcategories()) {
                subcategory.setStatus(Status.ACTIVE);

                // Activate all plans under this subcategory
                List<Plan> plans = planRepository.findBySubcategory(subcategory);
                for (Plan plan : plans) {
                    plan.setPlanStatus(Status.ACTIVE);
                    planRepository.save(plan);
                }
                subcategoryRepository.save(subcategory);
            }

            categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
    }

    public void deactivateCategory(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            
            // Deactivate the category
            category.setStatus(Status.INACTIVE);
            
            // Deactivate all associated subcategories
            for (Subcategory subcategory : category.getSubcategories()) {
                subcategory.setStatus(Status.INACTIVE);

                // Deactivate all plans under this subcategory
                List<Plan> plans = planRepository.findBySubcategory(subcategory);
                for (Plan plan : plans) {
                    plan.setPlanStatus(Status.INACTIVE);
                    planRepository.save(plan);
                }
                subcategoryRepository.save(subcategory);
            }

            categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with ID: " + categoryId);
        }
    }


}
