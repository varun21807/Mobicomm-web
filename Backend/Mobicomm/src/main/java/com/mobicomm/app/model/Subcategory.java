package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subcategories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory {

    @Id
    @Column(name = "subcategory_id")
    private String subcategoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

