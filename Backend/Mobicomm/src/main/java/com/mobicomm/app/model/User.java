package com.mobicomm.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "user_email")
    private String userEmail;
    
    @Column(name = "phone_number")
    private Long phoneNumber;
    
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    
    @Column(name = "account_status")
    private String accountStatus;
    
    @Column(name = "plan_expiry_date")
    private String planExpiryDate;
    
    @Column(name = "plan_status")
    private Long planStatus;
}

