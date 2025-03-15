package com.mobicomm.app.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @Column(name="admin_id")
    private String adminId;
    
    @Column(name="admin_name", nullable = false)
    private String name;
    
    @Column(name="admin_email", unique = true, nullable = false)
    private String email;

    @Column(name="admin_password", nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
}
