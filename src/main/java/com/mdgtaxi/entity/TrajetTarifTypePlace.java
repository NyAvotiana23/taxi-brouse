package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Trajet_Tarif_Type_Place")
public class TrajetTarifTypePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "id_type_place", nullable = false)
    private TypePlace typePlace;

    @Column(name = "tarif_unitaire", nullable = false, unique = true)
    private double tarifUnitaire;

    private double nombrePlace;
}
