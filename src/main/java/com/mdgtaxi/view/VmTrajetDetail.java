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
        t.id AS id_trajet,
        t.id_ligne,
        ld.ville_depart,
        ld.ville_arrivee,
        c.nom || ' ' || c.prenom AS chauffeur,
        t.id_chauffeur,
        v.immatriculation AS vehicule,
        v.marque || ' ' || v.modele AS modele_vehicule,
        t.id_vehicule,
        tsa.libelle_statut AS statut_trajet,
        t.datetime_depart,
        t.datetime_arrivee,
        t.nombre_passager,
        v.maximum_passager,
        t.frais_unitaire,
        CASE
            WHEN t.datetime_arrivee IS NOT NULL
                THEN EXTRACT(EPOCH FROM (t.datetime_arrivee - t.datetime_depart))/3600
            END AS duree_heures
    FROM Trajet t
             INNER JOIN VM_Ligne_Detail ld ON t.id_ligne = ld.id_ligne
             INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
             INNER JOIN Vehicule v ON t.id_vehicule = v.id
             LEFT JOIN VM_Trajet_Statut_Actuel tsa ON t.id = tsa.id_trajet
""")
@Synchronize({"Trajet", "VM_Ligne_Detail", "Chauffeur", "Vehicule", "VM_Trajet_Statut_Actuel"})
public class VmTrajetDetail implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "nombre_passager")
    private Integer nombrePassager;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "datetime_depart")
    private LocalDateTime datetimeDepart;

    @Column(name = "datetime_arrivee")
    private LocalDateTime datetimeArrivee;

    @Column(name = "frais_unitaire")
    private BigDecimal fraisUnitaire;

    @Column(name = "ville_depart")
    private String villeDepart;

    @Column(name = "ville_arrivee")
    private String villeArrivee;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "nom_chauffeur")
    private String nomChauffeur;

}