package com.mobicomm.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
   @Column(name = "plan_status")
   private NewUserStatus neUserStatus;
}
