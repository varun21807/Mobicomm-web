package com.mobicomm.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "new_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
    @Id
    @Column(name = "new_user_id")
    private String newUserId;
    
    @Column(name = "new_user_name")
    private String newUserName;
    
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "request_type")
    private String requestType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NewUserStatus neUserStatus;
    
    @OneToMany(mappedBy = "newUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("newUser-address")
    private List<Address> addresses;
}
