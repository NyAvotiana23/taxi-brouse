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
        l.id AS id_ligne,
        ld.ville_depart,
        ld.ville_arrivee,
        COUNT(DISTINCT t.id) AS nombre_trajets,
        COALESCE(AVG(vr.taux_remplissage_pct), 0) AS taux_remplissage_moyen,
        COALESCE(SUM(vtf.total_recette), 0) AS recette_totale,
        COALESCE(SUM(vtf.benefice), 0) AS benefice_total,
        COALESCE(AVG(vtf.benefice), 0) AS benefice_moyen_par_trajet
    FROM Ligne l
             INNER JOIN VM_Ligne_Detail ld ON l.id = ld.id_ligne
             LEFT JOIN Trajet t ON l.id = t.id_ligne
             LEFT JOIN VM_Trajet_Remplissage vr ON t.id = vr.id_trajet
             LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
    GROUP BY l.id, ld.ville_depart, ld.ville_arrivee
""")
@Synchronize({"Ligne", "VM_Ligne_Detail", "Trajet", "VM_Trajet_Remplissage", "VM_Trajet_Finance"})
public class VmPerformanceLigne implements Serializable {
    @Id
    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "ville_depart")
    private String villeDepart;

    @Column(name = "ville_arrivee")
    private String villeArrivee;

    @Column(name = "nombre_trajets")
    private Long nombreTrajets;

    @Column(name = "taux_remplissage_moyen")
    private BigDecimal tauxRemplissageMoyen;

    @Column(name = "recette_totale")
    private BigDecimal recetteTotale;

    @Column(name = "benefice_total")
    private BigDecimal beneficeTotal;

    @Column(name = "benefice_moyen_par_trajet")
    private BigDecimal beneficeMoyenParTrajet;
}