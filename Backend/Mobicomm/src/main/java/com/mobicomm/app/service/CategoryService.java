package com.mobicomm.app.service;

import com.mobicomm.app.exception.CategoryNotFoundException;
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
        Category latestCategory = categoryRepository.findTopByOrderByCategoryIdDesc();
        int nextId = 1;
        if (latestCategory != null) {
            String latestCategoryId = latestCategory.getCategoryId();
            String numberPart = latestCategoryId.substring(4);
            try {
                nextId = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                nextId = 1;
            }
        }
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

    public Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
    }

    public Category addCategory(Category category) {
        category.setCategoryId(generateCategoryId());
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
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
    }

    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    public void activateCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        category.setStatus(Status.ACTIVE);

        for (Subcategory subcategory : category.getSubcategories()) {
            subcategory.setStatus(Status.ACTIVE);
            List<Plan> plans = planRepository.findBySubcategory(subcategory);
            for (Plan plan : plans) {
                plan.setPlanStatus(Status.ACTIVE);
                planRepository.save(plan);
            }
            subcategoryRepository.save(subcategory);
        }
        categoryRepository.save(category);
    }

    public void deactivateCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        category.setStatus(Status.INACTIVE);

        for (Subcategory subcategory : category.getSubcategories()) {
            subcategory.setStatus(Status.INACTIVE);
            List<Plan> plans = planRepository.findBySubcategory(subcategory);
            for (Plan plan : plans) {
                plan.setPlanStatus(Status.INACTIVE);
                planRepository.save(plan);
            }
            subcategoryRepository.save(subcategory);
        }
        categoryRepository.save(category);
    }
}
