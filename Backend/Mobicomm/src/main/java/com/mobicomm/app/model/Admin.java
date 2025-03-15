package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")  // ✅ Make sure the table name is correct
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ Auto-increment with primary key
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
