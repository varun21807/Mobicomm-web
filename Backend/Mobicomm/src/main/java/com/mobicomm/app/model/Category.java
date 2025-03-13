package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subcategory> subcategories;
}
