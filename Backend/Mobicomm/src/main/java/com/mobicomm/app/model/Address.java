package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	 @Id
	    private String addressId;
	 
    private String street;
    
    private String state;
    
    private String city;
    
    private Long pincode;
    
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "new_user_id", referencedColumnName = "new_user_id", nullable = true)
    private NewUser newUser;
    
    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "admin_id", nullable = true)
    private Admin admin;
    
}
