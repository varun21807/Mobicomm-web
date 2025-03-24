package com.mobicomm.app.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeHistory {

    @Id
    private String id;

    @Column(nullable = false)
    private String phoneNumber; // Linking with user by phone instead of User entity

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    private LocalDateTime rechargeDate;

    private double amount; // Amount paid for the recharge

    @Column(nullable = false)
    private String paymentMethod; // Added payment method field
    
    @Column(nullable = false)
    private LocalDateTime expiryDate; // Store expiry date
    
    private String status; // Status of the recharge

}
