package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Vehicule_Tarif_Type_Place")
public class VehiculeTarifTypePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_voiture", nullable = false)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_type_place", nullable = false)
    private TypePlace typePlace;

    @Column(name = "tarif_unitaire", nullable = false, unique = true)
    private double tarifUnitaire;

    @Column(name = "nombre_place", nullable = false, unique = true)
    private double nombrePlace;
}
