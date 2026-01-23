package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        t.id AS id_trajet,
        vd.nom AS nom_ville_depart,
        va.nom AS nom_ville_arrive,
        v.immatriculation AS immatriculation_vehicule,
        CAST(t.datetime_depart AS DATE) AS date_depart,
        CAST(t.datetime_depart AS TIME) AS heure_depart,
        COALESCE(SUM(trd.nombre_places * vttp.tarif_unitaire), 0) AS montant_prevision_ticket,
        COALESCE(SUM(dd.nombre_repetition * dd.montant_unitaire), 0) AS montant_prevision_diffusion,
        COALESCE(SUM(trd.nombre_places * vttp.tarif_unitaire), 0) + COALESCE(SUM(dd.nombre_repetition * dd.montant_unitaire), 0) AS montant_chiffre_affaire_prevision
    FROM Trajet t
    INNER JOIN Ligne l ON t.id_ligne = l.id
    INNER JOIN Ville vd ON l.id_ville_depart = vd.id
    INNER JOIN Ville va ON l.id_ville_arrivee = va.id
    INNER JOIN Vehicule v ON t.id_vehicule = v.id
    LEFT JOIN Trajet_Reservation tr ON tr.id_trajet = t.id
    LEFT JOIN Trajet_Reservation_Details trd ON trd.id_trajet_reservation = tr.id
    LEFT JOIN Vehicule_Tarif_Type_Place vttp ON vttp.id_vehicule = t.id_vehicule AND vttp.id_type_place = trd.id_type_place
    LEFT JOIN Diffusion_Detail dd ON dd.id_trajet = t.id
    GROUP BY t.id, vd.nom, va.nom, v.immatriculation, t.datetime_depart
""")
@Synchronize({"Trajet", "Ligne", "Ville", "Vehicule", "Trajet_Reservation", "Trajet_Reservation_Details", "Vehicule_Tarif_Type_Place", "Diffusion_Detail"})
public class VmTrajetPrevisionCaTotal implements Serializable {
    
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "nom_ville_depart")
    private String nomVilleDepart;

    @Column(name = "nom_ville_arrive")
    private String nomVilleArrive;

    @Column(name = "immatriculation_vehicule")
    private String immatriculationVehicule;

    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "heure_depart")
    private LocalTime heureDepart;

    @Column(name = "montant_prevision_ticket")
    private BigDecimal montantPrevisionTicket;

    @Column(name = "montant_prevision_diffusion")
    private BigDecimal montantPrevisionDiffusion;

    @Column(name = "montant_chiffre_affaire_prevision")
    private BigDecimal montantChiffreAffairePrevision;
}