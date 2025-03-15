package com.mobicomm.app.repository;

import com.mobicomm.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	 @Query("SELECT c FROM Category c ORDER BY CAST(SUBSTRING(c.categoryId, 5) AS integer) DESC")
	    Category findTopByOrderByCategoryIdDesc();
    Category findByName(String name);
}
