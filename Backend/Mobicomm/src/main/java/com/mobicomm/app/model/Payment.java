package com.mobicomm.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	 @Id
	    private String transactionId;
	    
	    @OneToOne
	    @JoinColumn(name = "purchase_id", nullable = false)
	    private Purchase purchase;
	    
	    private double amount;
	    
	    private String status; // Always "SUCCESS" for now
	    
	    private LocalDateTime timestamp;
	    @PrePersist
	    protected void onCreate() {
	        this.timestamp = LocalDateTime.now();
	    }

}
