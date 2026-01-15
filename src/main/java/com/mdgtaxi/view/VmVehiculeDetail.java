package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        v.id AS id_vehicule,
        v.immatriculation,
        v.marque,
        v.modele,
        v.maximum_passager,
        v.capacite_carburant,
        v.depense_carburant_100km,
        vt.libelle AS vehicule_type_libelle,
        ct.libelle AS carburant_type_libelle,
        vsa.libelle_statut,
        vsa.date_mouvement AS date_dernier_statut
    FROM Vehicule v
             INNER JOIN Vehicule_Type vt ON v.id_type = vt.id
             INNER JOIN Carburant_Type ct ON v.id_type_carburant = ct.id
             LEFT JOIN (
                 SELECT DISTINCT ON (vms.id_vehicule)
                     vms.id_vehicule,
                     vs.libelle AS libelle_statut,
                     vms.date_mouvement
                 FROM Vehicule_Mouvement_Statut vms
                          INNER JOIN Vehicule_Statut vs ON vms.id_nouveau_statut = vs.id
                 ORDER BY vms.id_vehicule, vms.date_mouvement DESC
             ) vsa ON v.id = vsa.id_vehicule
""")
@Synchronize({"Vehicule", "Vehicule_Type", "Carburant_Type", "Vehicule_Mouvement_Statut", "Vehicule_Statut"})
public class VmVehiculeDetail implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "maximum_passager")
    private Integer maximumPassager;

    @Column(name = "capacite_carburant")
    private BigDecimal capaciteCarburant;

    @Column(name = "depense_carburant_100km")
    private BigDecimal depenseCarburant100km;

    @Column(name = "vehicule_type_libelle")
    private String vehiculeTypeLibelle;

    @Column(name = "carburant_type_libelle")
    private String carburantTypeLibelle;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_dernier_statut")
    private LocalDateTime dateDernierStatut;
}