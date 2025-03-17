package com.mobicomm.app.repository;

import com.mobicomm.app.model.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
	@Query(value = "SELECT * FROM address ORDER BY CAST(SUBSTRING(address_id, 6) AS SIGNED) DESC LIMIT 1", nativeQuery = true)
	Address findTopByOrderByAddressIdDesc();


    // Retrieves all addresses (standard method)
    List<Address> findAll();

}
