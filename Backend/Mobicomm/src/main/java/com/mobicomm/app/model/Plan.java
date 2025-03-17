package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "plans") // ✅ Changed to lowercase
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @Column(name = "plan_id")
    private String planId;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "validity")
    private int validity;

    @Column(name = "data")
    private String data;

    @Column(name = "calls")
    private String calls;

    @Column(name = "sms")
    private Integer sms;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "offer")
    private String offer;

    @Column(name = "badgeColor")
    private String badgeColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status")
    private Status planStatus; // ✅ Updated reference to the correct enum name

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    
    private Subcategory subcategory;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "plan_ott",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "ott_id")
    )
    private Set<Ott> ottPlatforms;
    
  
}
    


  
