package com.mobicomm.app.controller;

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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Subcategory addSubcategory(@RequestBody Subcategory subcategory) {
        return subcategoryService.addSubcategory(subcategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Subcategory updateSubcategory(@PathVariable String id, @RequestBody Subcategory updatedSubcategory) {
        return subcategoryService.updateSubcategory(id, updatedSubcategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSubcategory(@PathVariable String id) {
        subcategoryService.deleteSubcategory(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/deactivate")
    public void deactivateSubcategory(@PathVariable String id) {
        subcategoryService.deactivateSubcategory(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/activate")
    public void activateSubcategory(@PathVariable String id) {
        subcategoryService.activateSubcategory(id);
    }
}
