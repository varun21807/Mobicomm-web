package com.mobicomm.app.controller;

import com.mobicomm.app.model.Category;
import com.mobicomm.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ✅ Anyone (admin & user) can see categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable String id) {
        return categoryService.getCategoryById(id);
    }

    // ✅ Only Admin can create, update, delete
    @PreAuthorize("hasRole('ADMIN')")
 // Change this to match your token's role
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
  // Change this to match your token's role
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable String id, @RequestBody Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
  // Change this to match your token's role
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/active")
    public List<Category> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    @GetMapping("/inactive")
    public List<Category> getInactiveCategories() {
        return categoryService.getInactiveCategories();
    }

    @PreAuthorize("hasRole('ADMIN')")
  // Change this to match your token's role
    @PutMapping("/{id}/activate")
    public void activateCategory(@PathVariable String id) {
        categoryService.activateCategory(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
 // Change this to match your token's role
    @PutMapping("/{id}/deactivate")
    public void deactivateCategory(@PathVariable String id) {
        categoryService.deactivateCategory(id);
    }
}
