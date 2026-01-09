package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Vehicule")
public class Vehicule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_type", nullable = false)
    private VehiculeType vehiculeType;

    @ManyToOne
    @JoinColumn(name = "id_type_carburant", nullable = false)
    private CarburantType carburantType;

    @Column(name = "marque", nullable = false)
    private String marque;

    @Column(name = "modele", nullable = false)
    private String modele;

    @Column(name = "maximum_passager", nullable = false)
    private Integer maximumPassager;

    @Column(name = "immatriculation", nullable = false, unique = true)
    private String immatriculation;

    @Column(name = "capacite_carburant", precision = 10, scale = 2)
    private BigDecimal capaciteCarburant;

    @Column(name = "depense_carburant_100km", precision = 10, scale = 2)
    private BigDecimal depenseCarburant100km;
}