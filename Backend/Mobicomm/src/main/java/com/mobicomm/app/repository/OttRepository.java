package com.mobicomm.app.repository;

import com.mobicomm.app.model.Ott;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OttRepository extends JpaRepository<Ott, String> {
	 @Query("SELECT o FROM Ott o ORDER BY CAST(SUBSTRING(o.ottId, 5) AS integer) DESC")
	    Ott findTopByOrderByOttIdDesc();
    List<Ott> findByOttNameContainingIgnoreCase(String ottName);
}
