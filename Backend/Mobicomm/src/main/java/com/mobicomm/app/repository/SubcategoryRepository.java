package com.mobicomm.app.repository;

import com.mobicomm.app.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, String> {
	 @Query("SELECT s FROM Subcategory s ORDER BY CAST(SUBSTRING(s.subcategoryId, 5) AS integer) DESC")
	    Subcategory findTopByOrderBySubcategoryIdDesc();
    List<Subcategory> findByCategoryCategoryId(String categoryId);
}
