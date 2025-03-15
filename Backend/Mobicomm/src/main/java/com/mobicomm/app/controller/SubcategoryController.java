package com.mobicomm.app.controller;

import com.mobicomm.app.model.Subcategory;
import com.mobicomm.app.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public List<Subcategory> getAllSubcategories() {
        return subcategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public Optional<Subcategory> getSubcategoryById(@PathVariable String id) {
        return subcategoryService.getSubcategoryById(id);
    }

    @PostMapping
    public Subcategory addSubcategory(@RequestBody Subcategory subcategory) {
    	
        return subcategoryService.addSubcategory(subcategory);
    }

    @PutMapping("/{id}")
    public Subcategory updateSubcategory(@PathVariable String id, @RequestBody Subcategory updatedSubcategory) {
    
        return subcategoryService.updateSubcategory(id, updatedSubcategory);
    }

    @DeleteMapping("/{id}")
    public void deleteSubcategory(@PathVariable String id) {
        subcategoryService.deleteSubcategory(id);
    }
    @PutMapping("/{id}/deactivate")
    public void deactivateSubcategory(@PathVariable String id) {
        subcategoryService.deactivateSubcategory(id);
    }

    @PutMapping("/{id}/activate")
    public void activateSubcategory(@PathVariable String id) {
        subcategoryService.activateSubcategory(id);
    }
}
