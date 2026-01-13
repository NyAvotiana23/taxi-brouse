package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        v.id AS id_vehicule,
        v.immatriculation,
        v.marque,
        v.modele,
        COUNT(DISTINCT t.id) AS nombre_trajets,
        COALESCE(SUM(vce.total_depense_entretien), 0) AS cout_entretien_total,
        COALESCE(SUM(vtf.total_recette), 0) AS recette_totale,
        COALESCE(SUM(vtf.benefice), 0) AS benefice_total,
        COALESCE(AVG(EXTRACT(EPOCH FROM (t.datetime_arrivee - t.datetime_depart))/3600), 0) AS duree_moyenne_trajet_heures
    FROM Vehicule v
             LEFT JOIN Trajet t ON v.id = t.id_vehicule
             LEFT JOIN VM_Vehicule_Cout_Entretien vce ON v.id = vce.id_vehicule
             LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
    GROUP BY v.id, v.immatriculation, v.marque, v.modele
""")
@Synchronize({"Vehicule", "Trajet", "VM_Vehicule_Cout_Entretien", "VM_Trajet_Finance"})
public class VmPerformanceVehicule implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "nombre_trajets")
    private Long nombreTrajets;

    @Column(name = "cout_entretien_total")
    private BigDecimal coutEntretienTotal;

    @Column(name = "recette_totale")
    private BigDecimal recetteTotale;

    @Column(name = "benefice_total")
    private BigDecimal beneficeTotal;

    @Column(name = "duree_moyenne_trajet_heures")
    private BigDecimal dureeMoyenneTrajetHeures;
}