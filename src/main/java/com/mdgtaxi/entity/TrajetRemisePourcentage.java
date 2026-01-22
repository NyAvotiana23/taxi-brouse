package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Trajet_Remise_Pourcentage")
public class TrajetRemisePourcentage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "categorie_application", nullable = false)
    private CategoriePersonne categorieApplication;

    @ManyToOne
    @JoinColumn(name = "categorie_par_rapport", nullable = false)
    private CategoriePersonne categorieParRapport;

    @Column(name = "remisePourcent", nullable = false)
    private double remisePourcent;

}
