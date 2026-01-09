package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Vehicule_Mouvement_Statut")
public class VehiculeMouvementStatut implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @ManyToOne
    @JoinColumn(name = "id_ancien_statut")
    private VehiculeStatut ancienStatut;

    @ManyToOne
    @JoinColumn(name = "id_nouveau_statut", nullable = false)
    private VehiculeStatut nouveauStatut;

    @Column(name = "observation")
    private String observation;
}