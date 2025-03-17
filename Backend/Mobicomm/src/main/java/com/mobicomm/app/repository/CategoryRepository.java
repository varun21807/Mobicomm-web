package com.mobicomm.app.repository;

import com.mobicomm.app.model.Category;
import com.mobicomm.app.model.Status;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
	Category findTopByOrderByCategoryIdDesc();
	 // Find all categories by status
    List<Category> findByStatus(Status status);

    // Custom JPQL query to get categories by status
    @Query("SELECT c FROM Category c WHERE c.status = :status")
    List<Category> findCategoriesByStatus(@Param("status") Status status);

    // Native SQL query
    @Query(value = "SELECT * FROM category WHERE category_status = :status", nativeQuery = true)
    List<Category> findCategoriesByStatusNative(@Param("status") String status);
    Category findByName(String name);
}
