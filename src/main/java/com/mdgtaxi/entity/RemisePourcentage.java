package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Remise_Pourcentage")
public class RemisePourcentage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categorie_application", nullable = false)
    private CategoriePersonne categorieApplication;

    @ManyToOne
    @JoinColumn(name = "categorie_par_rapport", nullable = false)
    private CategoriePersonne categorieParRapport;

    @Column(name = "remisePourcent", nullable = false)
    private double remisePourcent;

}
