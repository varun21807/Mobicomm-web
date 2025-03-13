package com.mobicomm.app.repository;

import com.mobicomm.app.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, String> {

	 Subcategory findTopByOrderBySubcategoryIdDesc();
    List<Subcategory> findByCategoryCategoryId(String categoryId);
}
