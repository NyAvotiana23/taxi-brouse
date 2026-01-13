package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        vms.id,
        vms.id_vehicule,
        v.immatriculation,
        vs_ancien.libelle AS ancien_statut,
        vs_nouveau.libelle AS nouveau_statut,
        vms.date_mouvement,
        vms.observation
    FROM Vehicule_Mouvement_Statut vms
             INNER JOIN Vehicule v ON vms.id_vehicule = v.id
             LEFT JOIN Vehicule_Statut vs_ancien ON vms.id_ancien_statut = vs_ancien.id
             INNER JOIN Vehicule_Statut vs_nouveau ON vms.id_nouveau_statut = vs_nouveau.id
    ORDER BY vms.date_mouvement DESC
""")
@Synchronize({"Vehicule_Mouvement_Statut", "Vehicule", "Vehicule_Statut"})
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