package com.mobicomm.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "active_plans")
public class ActivePlan {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String phoneNumber; // Linking with user by phone instead of User entity

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
    
    private LocalDateTime activationDate;
    
    private LocalDateTime expiryDate;
    
  
}
