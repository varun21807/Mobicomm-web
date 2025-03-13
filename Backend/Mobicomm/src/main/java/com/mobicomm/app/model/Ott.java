package com.mobicomm.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ott")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ott {

    @Id
    @Column(name = "ott_id")	
    private String ottId;

    @Column(name = "ott_name", nullable = false)
    private String ottName;

    @Column(name = "icon_img")
    private String iconImg;

    @ManyToMany(mappedBy = "ottPlatforms", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Plan> plan;
}
