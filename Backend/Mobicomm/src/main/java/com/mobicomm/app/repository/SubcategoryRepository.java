package com.mobicomm.app.repository;

import com.mobicomm.app.model.Status;
import com.mobicomm.app.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, String> {
	@Query("SELECT s FROM Subcategory s ORDER BY s.subcategoryId DESC LIMIT 1")
	Optional<Subcategory> findTopByOrderBySubcategoryIdDesc();

	 // Find all subcategories by status
	    List<Subcategory> findByStatus(Status status);

	    // Find subcategories by category ID
	    List<Subcategory> findByCategory_CategoryId(String categoryId);

	    // Find active subcategories for a given category
	    @Query("SELECT s FROM Subcategory s WHERE s.category.categoryId = :categoryId AND s.status = 'ACTIVE'")
	    List<Subcategory> findActiveSubcategoriesByCategory(@Param("categoryId") String categoryId);

	    // Native SQL query
	    @Query(value = "SELECT * FROM subcategories WHERE category_id = :categoryId AND status = :status", nativeQuery = true)
	    List<Subcategory> findByCategoryAndStatus(@Param("categoryId") String categoryId, @Param("status") String status);
    List<Subcategory> findByCategoryCategoryId(String categoryId);
}
