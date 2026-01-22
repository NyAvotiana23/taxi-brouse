package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Trajet_Tarif_Type_Place_Categorie_Remise")
public class TrajetTarifTypePlaceCategorieRemise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "id_type_place", nullable = false)
    private TypePlace typePlace;

    @ManyToOne
    @JoinColumn(name = "id_categorie_personne", nullable = false)
    private CategoriePersonne categoriePersonne;

    @Column(name = "tarif_unitaire_avec_remise", nullable = false)
    private double tarifUnitaireAvecRemise;
}
