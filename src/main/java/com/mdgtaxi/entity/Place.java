package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false, unique = true)
    private String nom;

    @Column(name = "libelle", nullable = false, unique = true)
    private String description;
}
