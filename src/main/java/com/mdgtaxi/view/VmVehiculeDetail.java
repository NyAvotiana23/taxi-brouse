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
        vt.libelle AS libelle_type,
        ct.libelle AS type_carburant,
        vsa.libelle_statut,
        vsa.date_mouvement AS date_dernier_statut
    FROM Vehicule v
             INNER JOIN Vehicule_Type vt ON v.id_type = vt.id
             INNER JOIN Carburant_Type ct ON v.id_type_carburant = ct.id
             LEFT JOIN VM_Vehicule_Statut_Actuel vsa ON v.id = vsa.id_vehicule
""")
@Synchronize({"Vehicule", "Vehicule_Type", "Carburant_Type", "VM_Vehicule_Statut_Actuel"})
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

    @Column(name = "libelle_type")
    private String libelleType;

    @Column(name = "type_carburant")
    private String typeCarburant;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_dernier_statut")
    private LocalDateTime dateDernierStatut;
}