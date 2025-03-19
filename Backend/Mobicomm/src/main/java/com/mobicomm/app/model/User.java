package com.mobicomm.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "user_name", nullable = false)
    private String userName;
    
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;
    
    @Column(name = "account_status", nullable = false)
    private Status status;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-address")
    private List<Address> addresses;


    
}
