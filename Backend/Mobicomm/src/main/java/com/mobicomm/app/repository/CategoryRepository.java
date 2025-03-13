package com.mobicomm.app.repository;

import com.mobicomm.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

	 Category findTopByOrderByCategoryIdDesc();
    Category findByName(String name);
}
