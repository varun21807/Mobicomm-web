package com.mobicomm.app.repository;

import com.mobicomm.app.model.Address;
import java.util.List;



public interface AddressRepository {

	List<Address> findAll();

}
