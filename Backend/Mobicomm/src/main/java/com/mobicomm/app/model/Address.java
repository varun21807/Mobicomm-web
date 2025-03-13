package com.mobicomm.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public  class Address {
    @Id
    @Column(name="address_id")
    private String newUserId;
    
    private String street;
    
    private String city;
    
    private Long pincode;
    
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "new_user_id", referencedColumnName = "new_user_id", nullable = true)
    private NewUser newUser;
}

