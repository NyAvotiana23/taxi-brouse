package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Vehicule_Historique_Statut")
public class VmVehiculeHistoriqueStatut implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "ancien_statut")
    private String ancienStatut;

    @Column(name = "nouveau_statut")
    private String nouveauStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "observation")
    private String observation;
}