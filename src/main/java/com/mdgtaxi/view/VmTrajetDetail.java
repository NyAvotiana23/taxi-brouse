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
        vd.nom,
        vd.nom,
        c.nom || ' ' || c.prenom AS chauffeur,
        t.id_chauffeur,
        v.immatriculation AS vehicule,
        v.marque || ' ' || v.modele AS modele_vehicule,
        t.id_vehicule,
        tsa.libelle_statut,
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
             INNER JOIN Ligne l ON t.id_ligne = l.id
             INNER JOIN Ville vd ON l.id_ville_depart = vd.id
             INNER JOIN Ville va ON l.id_ville_arrivee = va.id
             INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
             INNER JOIN Vehicule v ON t.id_vehicule = v.id
             LEFT JOIN VM_Trajet_Statut_Actuel tsa ON t.id = tsa.id_trajet
""")
@Synchronize({"Trajet", "Ligne", "Ville", "Chauffeur", "Vehicule", "VM_Trajet_Statut_Actuel"})
public class VmTrajetDetail implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "ville_depart")
    private String villeDepart;

    @Column(name = "ville_arrivee")
    private String villeArrivee;

    @Column(name = "chauffeur")
    private String chauffeur;

    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "vehicule")
    private String vehicule;

    @Column(name = "modele_vehicule")
    private String modeleVehicule;

    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "datetime_depart")
    private LocalDateTime datetimeDepart;

    @Column(name = "datetime_arrivee")
    private LocalDateTime datetimeArrivee;

    @Column(name = "nombre_passager")
    private Integer nombrePassager;

    @Column(name = "maximum_passager")
    private Integer maximumPassager;

    @Column(name = "frais_unitaire")
    private BigDecimal fraisUnitaire;

    @Column(name = "duree_heures")
    private BigDecimal dureeHeures;
}