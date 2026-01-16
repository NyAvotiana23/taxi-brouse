package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Categorie_Personne")
public class CategoriePersonne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;
}
