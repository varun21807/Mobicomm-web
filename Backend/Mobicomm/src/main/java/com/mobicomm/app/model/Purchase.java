package com.mobicomm.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    
    @Id
    private String purchaseId; // Auto-generated TXN-YYYYMMDD-XXXX format
    
    @Column(nullable = false)
    private String phoneNumber; // Linking with user by phone instead of User entity

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
    
    private LocalDateTime purchaseDate;
    
    private LocalDateTime expiryDate; // Based on plan validity
    
    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL)
    private Payment payment;
}
