package com.mobicomm.app.controller;

import com.mobicomm.app.model.Status;
import com.mobicomm.app.model.Subcategory;
import com.mobicomm.app.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://127.0.0.1:5503")
@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    // ✅ Anyone can view subcategories
    @GetMapping
    public List<Subcategory> getAllSubcategories() {
        return subcategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public Optional<Subcategory> getSubcategoryById(@PathVariable String id) {
        return subcategoryService.getSubcategoryById(id);
    }

    // ✅ Only Admin can modify subcategories
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Subcategory addSubcategory(@RequestBody Subcategory subcategory) {
        return subcategoryService.addSubcategory(subcategory);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Subcategory updateSubcategory(@PathVariable String id, @RequestBody Subcategory updatedSubcategory) {
        return subcategoryService.updateSubcategory(id, updatedSubcategory);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSubcategory(@PathVariable String id) {
        subcategoryService.deleteSubcategory(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/deactivate")
    public void deactivateSubcategory(@PathVariable String id) {
        subcategoryService.deactivateSubcategory(id);
    }
    @GetMapping("/status/{status}")
    public List<Subcategory> getSubcategoriesByStatus(@PathVariable Status status) {
        return subcategoryService.getSubcategoriesByStatus(status);
    }

    @GetMapping("/category/{categoryId}")
    public List<Subcategory> getSubcategoriesByCategory(@PathVariable String categoryId) {
        return subcategoryService.getSubcategoriesByCategory(categoryId);
    }

    @GetMapping("/category/{categoryId}/active")
    public List<Subcategory> getActiveSubcategoriesByCategory(@PathVariable String categoryId) {
        return subcategoryService.getActiveSubcategoriesByCategory(categoryId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/activate")
    public void activateSubcategory(@PathVariable String id) {
        subcategoryService.activateSubcategory(id);
    }
}
