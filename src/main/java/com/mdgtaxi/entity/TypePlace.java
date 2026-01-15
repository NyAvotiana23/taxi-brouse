package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Type_Place")
public class TypePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_type_place", nullable = false, unique = true)
    private String nomTypePlace;

    @Column(name = "description", nullable = false, unique = true)
    private String description;
}
